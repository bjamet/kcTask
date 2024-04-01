package com.myKeyCons.tasks.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import com.myKeyCons.tasks.domain.exception.TaskNotFoundException;
import com.myKeyCons.tasks.domain.infrastructureitf.TaskRepositoryItf;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {

  @Mock
  private TaskRepositoryItf taskRepositoryItf;

  @InjectMocks
  private TaskService taskService;

  @Test
  void testGetAllTasks() throws Exception {
    List<TaskEntity> taskEntities =
        Arrays.asList(new TaskEntity("task1", "task1", false, "author"), new TaskEntity("task2", "task2", true, "author"));
    when(taskRepositoryItf.findAll(null)).thenReturn(taskEntities);
    when(taskRepositoryItf.findAll(true)).thenReturn(List.of(taskEntities.get(1)));
    when(taskRepositoryItf.findAll(false)).thenReturn(List.of(taskEntities.get(0)));

    List<TaskEntity> actualTasks = taskService.getAllTasks(null);
    assertEquals(actualTasks.size(), 2);
    assertTrue(actualTasks.contains(taskEntities.get(0)));
    assertTrue(actualTasks.contains(taskEntities.get(1)));

    actualTasks = taskService.getAllTasks(true);
    assertEquals(actualTasks.size(), 1);
    assertTrue(actualTasks.contains(taskEntities.get(1)));

    actualTasks = taskService.getAllTasks(false);
    assertEquals(actualTasks.size(), 1);
    assertTrue(actualTasks.contains(taskEntities.get(0)));
  }

  @Test
  void testGetTaskById() throws Exception {
    UUID taskId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    TaskEntity taskEntity = new TaskEntity(taskId.toString(), "task1", false, "author");
    when(taskRepositoryItf.findById(taskId.toString())).thenReturn(Optional.of(taskEntity));

    TaskEntity actualTask = taskService.getTaskById(taskId.toString());
    assertEquals(actualTask, taskEntity);
  }

  @Test
  void testGetTaskByIdWithNotFound() throws Exception {
    UUID taskId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    when(taskRepositoryItf.findById(taskId.toString())).thenReturn(Optional.empty());

    try {
      taskService.getTaskById(taskId.toString());
      fail("Expected TaskNotFoundException");
    } catch (TaskNotFoundException e) {
      assertEquals(e.getMessage(), "Task not found");
    }
  }

  @Test
  void testCreateTask()  {
    ArgumentCaptor<TaskEntity> valueCapture = ArgumentCaptor.forClass(TaskEntity.class);
    doNothing().when(taskRepositoryItf).save(valueCapture.capture());

    TaskEntity actualTask = taskService.createTask("task1", "author");
    assertEquals("task1",valueCapture.getValue().getLabel());
    assertEquals(false,valueCapture.getValue().getComplete());
    assertEquals("author",valueCapture.getValue().getAuthor());

  }

  @Test
  void testUpdateTask() throws Exception {
    ArgumentCaptor<TaskEntity> valueCapture = ArgumentCaptor.forClass(TaskEntity.class);
    doNothing().when(taskRepositoryItf).save(valueCapture.capture());

    UUID taskId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    TaskEntity taskEntity = new TaskEntity(taskId.toString(), "task1", false, "author");


    TaskEntity actualTask = taskService.updateTask(taskEntity);
    assertEquals(actualTask, taskEntity);
    assertEquals(actualTask, valueCapture.getValue());
  }

  @Test
  void testDeleteTask() throws Exception {
    UUID taskId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    TaskEntity taskEntity = new TaskEntity(taskId.toString(), "task1", false, "author");

    when(taskRepositoryItf.deleteById(taskId.toString())).thenReturn(Optional.of(taskEntity));

    taskService.deleteTask(taskId.toString());
    verify(taskRepositoryItf).deleteById(any());
  }

  @Test
  void testDeleteTaskWithNotFound() throws Exception {
    UUID taskId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    when(taskRepositoryItf.deleteById(taskId.toString())).thenReturn(Optional.empty());

    try {
      taskService.deleteTask(taskId.toString());
      fail("Expected TaskNotFoundException");
    } catch (TaskNotFoundException e) {
      assertEquals(e.getMessage(), "Task not found");
    }
    verify(taskRepositoryItf).deleteById(any());
  }
}
