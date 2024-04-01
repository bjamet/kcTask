package com.myKeyCons.tasks.domain.service;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import com.myKeyCons.tasks.domain.infrastructureitf.TaskRepositoryItf;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  private TaskRepositoryItf taskRepositoryItf;

  public TaskService(TaskRepositoryItf taskRepositoryItf) {
    this.taskRepositoryItf = taskRepositoryItf;
  }

  public List<TaskEntity> getAllTasks() {
    return taskRepositoryItf.findAll();
  }

  public TaskEntity createTask(String label, String description, String author) {
    TaskEntity entity = new TaskEntity(UUID.randomUUID().toString(), label, description, author);
    taskRepositoryItf.save(entity);
    return entity;
  }

  public TaskEntity getTaskById(String id) {
    return taskRepositoryItf.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
  }

  public TaskEntity updateTask(TaskEntity task) {
    taskRepositoryItf.save(task);
    return task;
  }

  public void deleteTask(String id) {
    taskRepositoryItf.deleteById(id).orElseThrow(() -> new RuntimeException("Task not found"));
  }
}
