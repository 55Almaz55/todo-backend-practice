package com.todo.demo.ControllersTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.demo.Controllers.UserController;
import com.todo.demo.Models.User;
import com.todo.demo.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // подменяем сервис моками
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User("Almaz", "almaz@example.com");
        user1.setId(1);
    }

    // CREATE
    @Test
    void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Almaz"))
                .andExpect(jsonPath("$.email").value("almaz@example.com"));
    }

    // READ all
    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(user1));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Almaz"));
    }

    // READ by id
    @Test
    void testGetUserById() throws Exception {
        Mockito.when(userService.getUserById(1)).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Almaz"));
    }

    // UPDATE
    @Test
    void testUpdateUser() throws Exception {
        User updated = new User("Updated", "updated@example.com");
        updated.setId(1);

        Mockito.when(userService.updateUser(eq(1), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    // DELETE
    @Test
    void testDeleteUser() throws Exception {
        Mockito.when(userService.deleteUser(1)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));
    }
}
