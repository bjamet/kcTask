package com.myKeyCons.tasks.controller;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import com.myKeyCons.tasks.domain.service.TaskService;
import com.myKeyCons.tasks.dto.TaskDTO;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
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
  public TaskDTO createTask(Principal principal, @RequestBody TaskDTO task) {
    // TODO authod from session

    return TaskDTO.fromTaskEntity(taskService.createTask(task.getLabel(), task.getDescription(), task.getAuthor()));
  }

  @GetMapping("/{id}")
  public TaskDTO getTaskById(@PathVariable String id) {
    return TaskDTO.fromTaskEntity(taskService.getTaskById(id));
  }

  @PutMapping("/{id}")
  public TaskDTO updateTask(@RequestBody TaskDTO task, @PathVariable String id) {
    TaskEntity taskEntity = taskService.getTaskById(id);
    // TODO check author
    taskEntity.setDescription(task.getDescription());
    taskEntity.setLabel(task.getLabel());
    return TaskDTO.fromTaskEntity(taskService.updateTask(taskEntity));
  }

  @DeleteMapping("/{id}")
  public void deleteTask(@PathVariable String id) {
    // check author
    taskService.deleteTask(id);
  }
}
