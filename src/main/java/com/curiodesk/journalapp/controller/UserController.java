package com.curiodesk.journalapp.controller;

import com.curiodesk.journalapp.entity.User;
import com.curiodesk.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {this.userService = userService;}

    @GetMapping
    public List<User> getUsers() {
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("User " + user.getUsername() + " has been created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username,
                                             @RequestBody User user) {
        User updatedUser = userService.updateuser(username, user);
        if (updatedUser != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("User " + updatedUser.getUsername() + " has been updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
