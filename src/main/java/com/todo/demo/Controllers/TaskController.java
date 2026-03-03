package com.todo.demo.Controllers;

import com.todo.demo.Models.Task;
import com.todo.demo.Models.User;
import com.todo.demo.Services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Tasks")
public class TaskController {
    private TaskService taskService;
    public TaskController(TaskService taskService) {this.taskService = taskService;}

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public  Task getTask(@PathVariable long id) {
        return taskService.getTaskById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable long id,@RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable long id) {
        boolean taskDeleted = taskService.deleteTask(id);
        return taskDeleted ? "Task deleted" : "Task with this ID not found" ;
    }
}
