package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.entity.User;
import com.curiodesk.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateuser(final String username, final User user) {
        User userInDb = findByUsername(username);
        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            return save(userInDb);
        }
        return null;
    }

    public Optional<User> findById(ObjectId journalId) {
        return userRepository.findById(journalId);
    }

    public void deleteById(ObjectId userId) {
        userRepository.deleteById(userId);
    }

}
