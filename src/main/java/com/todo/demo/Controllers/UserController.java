package com.todo.demo.Controllers;

import com.todo.demo.Models.User;
import com.todo.demo.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    // CREATE (POST) — добавить пользователя
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    // READ (GET) — получить всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // READ (GET) — получить конкретного пользователя
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUserById(id).orElse(null);
    }

    // UPDATE (PUT) — обновить имя пользователя
    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @Valid @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // DELETE (DELETE) — удалить пользователя
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id){
        boolean deleted = userService.deleteUser(id);
        return deleted ? "User deleted" : "User with this ID not found";
    }
}
