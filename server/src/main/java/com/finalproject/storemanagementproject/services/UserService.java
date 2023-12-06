package com.finalproject.storemanagementproject.services;

import com.finalproject.storemanagementproject.models.Role;
import com.finalproject.storemanagementproject.models.User;
import com.finalproject.storemanagementproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean addUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) return false;

        userRepository.save(user);
        return true;
    }

    public boolean updateUser(User user) {
        if (userRepository.findById(user.getId()).orElse(null) == null) return false;

        userRepository.save(user);
        return true;
    }

    public boolean isValidRole(String role) {
        for (Role r : Role.values())
            if (r.name().equals(role))
                return true;

        return false;
    }
}
