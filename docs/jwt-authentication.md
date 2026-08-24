# JWT Authentication Flow

## Overview

This application uses **stateless JWT (JSON Web Token) based authentication**. There are no server-side sessions. Instead, the server issues a signed token on login, and the client sends that token with every subsequent request to prove its identity.

---

## Components Involved

| Class | Package | Responsibility |
|---|---|---|
| `PublicController` | `controller` | Exposes `/login` and `/signup` endpoints |
| `AuthenticationManager` | Spring Security | Validates username + password against the database |
| `DaoAuthenticationProvider` | Spring Security / `SecurityConfig` | Wires `UserDetailsServiceImpl` + `BCryptPasswordEncoder` for credential validation |
| `UserDetailsServiceImpl` | `service` | Loads a `User` from MongoDB by username |
| `JwtUtil` | `util` | Creates, signs, and validates JWT tokens |
| `JwtFilter` | `security` | Intercepts every HTTP request, extracts and validates the JWT |
| `SecurityConfig` | `security` | Defines which routes are public/protected, registers `JwtFilter` in the filter chain |

---

## Phase 1 — User Registration (`POST /api/public/signup`)

```
Client ──POST /api/public/signup { username, password }──► PublicController
         └─► UserService.createNewUser()
               └─► BCryptPasswordEncoder hashes the password
               └─► UserRepository.save(user) ──► MongoDB
         ◄── 201 Created: "User <username> has been created"
```

1. Client sends `username` and plaintext `password` in the request body.
2. `UserService.createNewUser()` hashes the password with `BCryptPasswordEncoder` before saving.
3. The user document is stored in the MongoDB `users` collection with a **hashed** password — the plaintext is never persisted.
4. Roles are assigned during creation (e.g., `USER`, `ADMIN`).

---

## Phase 2 — Login and Token Issuance (`POST /api/public/login`)

```
Client ──POST /api/public/login { username, password }──► PublicController
         └─► AuthenticationManager.authenticate(UsernamePasswordAuthenticationToken)
               └─► DaoAuthenticationProvider
                     └─► UserDetailsServiceImpl.loadUserByUsername(username)
                           └─► UserRepository.findByUsername(username) ──► MongoDB
                     └─► BCryptPasswordEncoder.matches(rawPassword, hashedPassword)
                     └─► throws BadCredentialsException if mismatch
         └─► JwtUtil.generateToken(username)
               └─► Jwts.builder()
                     .subject(username)
                     .issuedAt(now)
                     .expiration(now + 60 minutes)
                     .signWith(HMAC-SHA key)
                     .compact()
         ◄── 200 OK: "<jwt-token-string>"
```

### What's inside the token?

A JWT has three Base64-encoded parts separated by dots: `header.payload.signature`

**Header:**
```json
{ "typ": "JWT", "alg": "HS256" }
```

**Payload (claims):**
```json
{
  "sub": "john_doe",
  "iat": 1724497800,
  "exp": 1724501400
}
```
- `sub` — the username (used to identify the user on subsequent requests)
- `iat` — issued-at timestamp (Unix epoch)
- `exp` — expiry timestamp (60 minutes after `iat`)

**Signature:**
```
HMACSHA256(base64(header) + "." + base64(payload), SECRET_KEY)
```

The signature is computed using a hardcoded 256-bit hex key. This ensures no one can forge or tamper with a token — any modification to the header or payload would invalidate the signature.

---

## Phase 3 — Authenticated Request Flow

Every request to a protected endpoint goes through the Spring Security **filter chain**. `JwtFilter` runs **before** `UsernamePasswordAuthenticationFilter`.

```
Client ──GET /api/journal (Authorization: Bearer <token>)──► JwtFilter
│
├─ 1. Extract header: request.getHeader("Authorization")
│       → "Bearer eyJhbGci..."
│
├─ 2. Strip prefix: token = authHeader.substring(7)
│       → "eyJhbGci..."
│
├─ 3. JwtUtil.extractUsername(token)
│       → Parses claims, verifies signature with SECRET_KEY
│       → Returns "john_doe"
│
├─ 4. Check SecurityContext: is user already authenticated?
│       → SecurityContextHolder.getContext().getAuthentication() == null?
│       → If already set (e.g. re-entrant), skip to filterChain.doFilter()
│
├─ 5. UserDetailsServiceImpl.loadUserByUsername("john_doe")
│       → Hits MongoDB: db.users.findOne({ username: "john_doe" })
│       → Builds Spring Security UserDetails (username, hashed password, roles)
│
├─ 6. JwtUtil.validateToken(token)
│       → extractExpiration(token).before(new Date())
│       → Returns false if expired, true if still valid
│
├─ 7. If valid:
│       → Create UsernamePasswordAuthenticationToken(userDetails, null, authorities)
│       → Set request details (IP, session info) via WebAuthenticationDetailsSource
│       → SecurityContextHolder.getContext().setAuthentication(authToken)
│           ↳ Spring Security now considers this request fully authenticated
│
├─ 8. filterChain.doFilter(request, response)
│       → Pass to next filter / dispatcher servlet
│
▼
SecurityConfig route rules evaluated:
  /api/public/**   → permitAll  (no auth needed)
  /api/admin/**    → hasRole("ADMIN")
  any other route  → authenticated()
        ↓
Controller method executed with authenticated principal available
```

### What happens if the token is missing or invalid?

| Scenario | JwtFilter behaviour | Result |
|---|---|---|
| No `Authorization` header | `authHeader` is null, skips all token logic | `SecurityContext` stays empty → Spring rejects with `401` |
| Header doesn't start with `Bearer ` | Same as above | `401` |
| Token signature is tampered | `Jwts.parser()` throws `JwtException` | Unhandled exception → `500` (or `401` if an exception handler is added) |
| Token is expired | `validateToken` returns `false` | `SecurityContext` not set → `401` |
| Username in token not in DB | `loadUserByUsername` throws `UsernameNotFoundException` | `500` (or `401` with exception handling) |
| Valid token, route needs `ADMIN` role | Auth is set but lacks `ROLE_ADMIN` | Spring rejects with `403 Forbidden` |

---

## Route Authorization Rules (SecurityConfig)

```java
.requestMatchers("/api/public/**").permitAll()       // login, signup, health-check — no token needed
.requestMatchers("/api/admin/**").hasRole("ADMIN")   // token required + ROLE_ADMIN
.anyRequest().authenticated()                        // all other routes require a valid token
```

The `hasRole("ADMIN")` check is against Spring Security's `GrantedAuthority` list, which is built from the `roles` field on the `User` entity. Spring automatically prefixes roles with `ROLE_`, so a stored role of `"ADMIN"` becomes the authority `ROLE_ADMIN`.

---

## Token Lifecycle

```
[POST /login] ──► Server issues token (valid 60 min)
                        │
                        ▼
              Client stores token (e.g. localStorage, memory)
                        │
                        ▼ (each request)
              Client sends: Authorization: Bearer <token>
                        │
                        ▼
              JwtFilter validates expiry + signature
                        │
              ┌─────────┴──────────┐
           valid                expired
              │                    │
       request proceeds       401 Unauthorized
                             (client must login again)
```

There is currently **no token refresh mechanism**. Once a token expires, the user must log in again to obtain a new one.

---

## Security Considerations

| Concern | Current State |
|---|---|
| Password storage | ✅ BCrypt hashed, never stored in plaintext |
| Token signing | ✅ HMAC-SHA256 — tamper-proof |
| Token expiry | ✅ 60-minute expiry |
| Secret key storage | ⚠️ Hardcoded in `JwtUtil.SECRET_KEY` — should be moved to `application.properties` or an environment variable |
| Token-to-user binding | ⚠️ `validateToken` only checks expiry, not that the token's subject matches the loaded user |
| Token revocation | ❌ No blacklist — a stolen token is valid until expiry |
| CSRF | ✅ Disabled (correct for stateless JWT APIs) |
| Session creation | ⚠️ Session policy not explicitly set to `STATELESS` in `SecurityConfig` |
