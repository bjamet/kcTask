package com.myKeyCons.tasks.domain.service;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import com.myKeyCons.tasks.domain.exception.TaskNotFoundException;
import com.myKeyCons.tasks.domain.infrastructureitf.TaskRepositoryItf;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  private TaskRepositoryItf taskRepositoryItf;

  public TaskService(TaskRepositoryItf taskRepositoryItf) {
    this.taskRepositoryItf = taskRepositoryItf;
  }

  public List<TaskEntity> getAllTasks(Boolean complete) {
    return taskRepositoryItf.findAll(complete);
  }

  public TaskEntity createTask(String label,  String author) {
    TaskEntity entity = new TaskEntity(UUID.randomUUID().toString(), label, false, author);
    taskRepositoryItf.save(entity);
    return entity;
  }

  public TaskEntity getTaskById(String id) throws TaskNotFoundException {
    return taskRepositoryItf.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
  }

  public TaskEntity updateTask(TaskEntity task) {
    taskRepositoryItf.save(task);
    return task;
  }

  public void deleteTask(String id) throws TaskNotFoundException {
    taskRepositoryItf.deleteById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
  }

}
