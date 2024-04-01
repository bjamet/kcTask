package com.myKeyCons.tasks.controller;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import com.myKeyCons.tasks.domain.exception.TaskNotFoundException;
import com.myKeyCons.tasks.domain.service.TaskService;
import com.myKeyCons.tasks.dto.TaskDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  private TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<TaskDTO> getAllTasks() {
    return taskService.getAllTasks().stream().map(TaskDTO::fromTaskEntity).collect(Collectors.toList());
  }

  @PostMapping
  public TaskDTO createTask(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TaskDTO task) {
    // create a task with the authenticated user as author
    return TaskDTO.fromTaskEntity(taskService.createTask(
        task.getLabel(),
        task.getDescription(),
        userDetails.getUsername()));
  }

  @GetMapping("/{id}")
  public TaskDTO getTaskById(@PathVariable String id) {
    try {
      return TaskDTO.fromTaskEntity(taskService.getTaskById(id));
    } catch (TaskNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }
  }

  @PutMapping("/{id}")
  public TaskDTO updateTask(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody TaskDTO task,
      @PathVariable String id) {
    TaskEntity taskEntity = null;
    try {
      taskEntity = taskService.getTaskById(id);
    } catch (TaskNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }
    if (!userDetails.getUsername().equals(taskEntity.getAuthor())){
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User is not the author");
    }
    taskEntity.setDescription(task.getDescription());
    taskEntity.setLabel(task.getLabel());
    return TaskDTO.fromTaskEntity(taskService.updateTask(taskEntity));
  }

  @DeleteMapping("/{id}")
  public void deleteTask(@AuthenticationPrincipal UserDetails userDetails,@PathVariable String id) {
    try {
      TaskEntity taskEntity = taskService.getTaskById(id);
      if (!userDetails.getUsername().equals(taskEntity.getAuthor())){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User is not the author");
      }
      taskService.deleteTask(id);
    } catch (TaskNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }
  }
}
