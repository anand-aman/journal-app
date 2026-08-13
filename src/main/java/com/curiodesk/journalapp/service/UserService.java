package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.entity.User;
import com.curiodesk.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User createNewUser(User user) {
        user.setRoles(new ArrayList<>(List.of("USER")));
        user.setPassword(Objects.requireNonNull(passwordEncoder.encode(user.getPassword())));
        return save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(final User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String username = authentication.getName();
        User userInDb = findByUsername(username);
        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(Objects.requireNonNull(passwordEncoder.encode(user.getPassword())));
            return save(userInDb);
        }
        return null;
    }

    public void deleteById(ObjectId userId) {
        userRepository.deleteById(userId);
    }

}
