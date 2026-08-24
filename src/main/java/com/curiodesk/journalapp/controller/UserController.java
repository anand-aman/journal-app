package com.curiodesk.journalapp.controller;

import com.curiodesk.journalapp.api.response.WeatherResponse;
import com.curiodesk.journalapp.entity.User;
import com.curiodesk.journalapp.service.UserService;
import com.curiodesk.journalapp.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final WeatherService  weatherService;

    public UserController(UserService userService, WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("User " + updatedUser.getUsername() + " has been updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        boolean isDeleted = userService.deleteUser();
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> greeting() {
        WeatherResponse weatherResponse = weatherService.getWeather("Bengaluru");
        if (weatherResponse != null) {
            return ResponseEntity.ok("Hello, " + getCurrentUsername() + "! Weather in Bengaluru feels like "
                    + weatherResponse.current().feelsLike() + "°C");
        }
        return ResponseEntity.ok("Hello, " + getCurrentUsername() + "!");
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
