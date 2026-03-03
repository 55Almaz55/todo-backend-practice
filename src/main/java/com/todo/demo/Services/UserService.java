package com.todo.demo.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todo.demo.Models.User;
import com.todo.demo.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user){
        logger.info("Creating user: {}", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    public User updateUser(Integer id, User updatedUser){
        return userRepository.findById(id).map(user -> {
            logger.info("Updating user with id {}. Old name: {}, new name: {}", id, user.getName(), updatedUser.getName());
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            User saved = userRepository.save(user);
            logger.debug("User with id {} successfully updated.", id);
            return saved;
        }).orElseGet(() -> {
            logger.error("User with id {} not found for update.", id);
            return null;
        });
    }

    public boolean deleteUser(Integer id){
        if(userRepository.existsById(id)) {
            logger.warn("Deleting user with id {}", id);
            userRepository.deleteById(id);
            return true;
        }
        logger.error("User with id {} not found for deletion", id);
        return false;
    }
}
