package com.todo.demo.Controllers;

import com.todo.demo.Models.Task;
import com.todo.demo.Models.User;
import com.todo.demo.Repository.UserRepository;
import com.todo.demo.Services.TaskService;
import com.todo.demo.Services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/tasks")
public class UserTaskController {

    private final UserService userService;
    private final TaskService taskService;

    public UserTaskController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    // Создать задачу для пользователя
    @PostMapping
    public Task createTaskForUser(@PathVariable int userId, @RequestBody Task task) {
        User user = userService.getUserById(userId).orElseThrow();
        task.setUser(user);
        return taskService.createTask(task);
    }

    // Получить все задачи пользователя
    @GetMapping
    public List<Task> getTasksForUser(@PathVariable int userId) {
        User user = userService.getUserById(userId).orElseThrow();
        return taskService.getTasksByUser(user);
    }

    // Получить задачу по ID
    @GetMapping("/{taskId}")
    public Task getTaskForUser(@PathVariable int userId, @PathVariable long taskId) {
        User user = userService.getUserById(userId).orElseThrow();
        return taskService.getTaskById(taskId)
                .filter(task -> task.getUser().getId() == user.getId())
                .orElse(null);
    }

    // Обновить задачу
    @PutMapping("/{taskId}")
    public Task updateTaskForUser(@PathVariable int userId, @PathVariable long taskId, @RequestBody Task updatedTask) {
        User user = userService.getUserById(userId).orElseThrow();
        updatedTask.setUser(user);
        return taskService.updateTask(taskId, updatedTask);
    }

    // Удалить задачу
    @DeleteMapping("/{taskId}")
    public String deleteTaskForUser(@PathVariable int userId, @PathVariable long taskId) {
        boolean deleted = taskService.deleteTask(taskId);
        return deleted ? "Task deleted" : "Task not found";
    }
}