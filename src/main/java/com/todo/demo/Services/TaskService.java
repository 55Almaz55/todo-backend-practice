package com.todo.demo.Services;

import com.todo.demo.Models.Task;
import com.todo.demo.Models.User;
import com.todo.demo.Repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {this.taskRepository = taskRepository;}

    public List<Task> getTasksByUser(User user){return taskRepository.findByUser(user);}

    public Task createTask(Task task) {
        logger.info("Creating task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {return taskRepository.findAll();}

    public Optional<Task> getTaskById(long id) {return taskRepository.findById(id);}

    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            logger.info("Updating task id {} from title '{}' to '{}'", id, task.getTitle(), updatedTask.getTitle());
            task.setTitle(updatedTask.getTitle());
            task.setCompleted(updatedTask.isCompleted());
            Task saved = taskRepository.save(task);
            logger.debug("Task id {} updated successfully", id);
            return saved;
        }).orElseGet(() -> {
            logger.error("Task id {} not found for update", id);
            return null;
        });
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            logger.warn("Deleting task with id {}", id);
            taskRepository.deleteById(id);
            return true;
        }
        logger.error("User with id {} not found for deletion", id);
        return false;
    }
}
