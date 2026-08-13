package com.curiodesk.journalapp.controller;

import com.curiodesk.journalapp.entity.User;
import com.curiodesk.journalapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final UserService userService;

    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String healthCheck() {return "OK";}

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createNewUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("User " + createdUser.getUsername() + " has been created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
