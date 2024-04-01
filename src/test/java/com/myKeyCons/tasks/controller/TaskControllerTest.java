package com.myKeyCons.tasks.controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import com.myKeyCons.tasks.domain.exception.TaskNotFoundException;
import com.myKeyCons.tasks.domain.service.TaskService;
import com.myKeyCons.tasks.dto.TaskDTO;
import jakarta.annotation.Nullable;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

  @Autowired
  private TaskController taskController;

  @MockBean
  private TaskService taskService;

  @Test
  void testGetAllTasks_whenNoTasksExist() {
    when(taskService.getAllTasks(any())).thenReturn(new ArrayList<>());

    List<TaskDTO> actualTasks = taskController.getAllTasks(null);

    assertTrue(actualTasks.isEmpty());
  }

  @Test
  void testGetAllTasks_whenTasksExist() {
    List<TaskEntity> expectedTasks = new ArrayList<>();
    expectedTasks.add(new TaskEntity(UUID.randomUUID().toString(), "test1", true, "author"));
    expectedTasks.add(new TaskEntity(UUID.randomUUID().toString(), "test2", false, "author"));
    when(taskService.getAllTasks(any())).thenReturn(expectedTasks);

    List<TaskDTO> actualTasks = taskController.getAllTasks(null);

    assertEquals(expectedTasks.size(), actualTasks.size());
    for (int i = 0; i < expectedTasks.size(); i++) {
      TaskEntity expectedTask = expectedTasks.get(i);
      TaskDTO actualTask = actualTasks.get(i);
      assertEquals(expectedTask.getId(), actualTask.getId());
      assertEquals(expectedTask.getLabel(), actualTask.getLabel());
      assertEquals(expectedTask.getComplete(), actualTask.getComplete());
    }
  }

  @Test
  void testGetTaskById_whenTaskExists() throws TaskNotFoundException {
    TaskEntity expectedTask = new TaskEntity(UUID.randomUUID().toString(), "test", false, "author");
    when(taskService.getTaskById(any())).thenReturn(expectedTask);

    TaskDTO actualTask = taskController.getTaskById("1");

    assertEquals(expectedTask.getId(), actualTask.getId());
    assertEquals(expectedTask.getLabel(), actualTask.getLabel());
    assertEquals(expectedTask.getComplete(), actualTask.getComplete());
  }

  @Test
  void testGetTaskById_whenTaskDoesNotExist() throws TaskNotFoundException {
    when(taskService.getTaskById(any())).thenThrow(new TaskNotFoundException("Task not found"));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> taskController.getTaskById("1"));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("404 NOT_FOUND \"Task not found\"", exception.getMessage());
  }

  @Test
  void testUpdateTask_whenTaskExists() throws TaskNotFoundException {
    TaskEntity expectedTask = new TaskEntity(UUID.randomUUID().toString(), "test", false, "author");
    when(taskService.getTaskById(any())).thenReturn(expectedTask);
    when(taskService.updateTask(any())).thenAnswer((i)-> i.getArgument(0));

    TaskDTO taskDTO = new TaskDTO("1", "updatedLabel", true, null);
    UserDetails userDetails = getUserDetails("author");
    TaskDTO actualTask = taskController.updateTask(userDetails, taskDTO, "1");

    assertEquals(expectedTask.getId(), actualTask.getId());
    assertEquals("updatedLabel", actualTask.getLabel());
    // check complete can't be updated this way
    assertFalse(actualTask.getComplete());
  }

  private static UserDetails getUserDetails(String username) {
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn(username);
    return userDetails;
  }

  @Test
  void testUpdateTask_whenTaskDoesNotExist() throws TaskNotFoundException {
    when(taskService.getTaskById(any())).thenThrow(new TaskNotFoundException("Task not found"));
    UserDetails userDetails = getUserDetails("author");
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> taskController.updateTask(userDetails, new TaskDTO("1", "label", false, "author"), "1"));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("404 NOT_FOUND \"Task not found\"", exception.getMessage());
  }

  @Test
  void testUpdateTask_whenUserIsNotTheAuthor() throws TaskNotFoundException {
    TaskEntity expectedTask = new TaskEntity(UUID.randomUUID().toString(), "test", false, "author");
    when(taskService.getTaskById(any())).thenReturn(expectedTask);
    UserDetails userDetails = getUserDetails("other");
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> taskController.updateTask(userDetails, new TaskDTO("1", "label", false, "author"), "1"));

    assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    assertEquals("403 FORBIDDEN \"User is not the author\"", exception.getMessage());
  }

  @Test
  void testCompleteTask_whenTaskExists() throws TaskNotFoundException {
    TaskEntity expectedTask = new TaskEntity(UUID.randomUUID().toString(), "test", false, "author");
    when(taskService.getTaskById(any())).thenReturn(expectedTask);
    when(taskService.updateTask(any())).thenAnswer((i)-> i.getArgument(0));

    UserDetails userDetails = getUserDetails("author");
    TaskDTO actualTask = taskController.completeTask(userDetails, "1");

    assertEquals(expectedTask.getId(), actualTask.getId());
    assertTrue(actualTask.getComplete());
  }

  @Test
  void testUnCompleteTask_whenTaskExists() throws TaskNotFoundException {
    TaskEntity expectedTask = new TaskEntity(UUID.randomUUID().toString(), "test", true, "author");
    when(taskService.getTaskById(any())).thenReturn(expectedTask);
    when(taskService.updateTask(any())).thenAnswer((i)-> i.getArgument(0));

    UserDetails userDetails = getUserDetails("author");
    TaskDTO actualTask = taskController.unCompleteTask(userDetails, "1");

    assertEquals(expectedTask.getId(), actualTask.getId());
    assertFalse(actualTask.getComplete());
  }

  @Test
  void testDeleteTask_whenTaskExists() throws TaskNotFoundException {
    TaskEntity expectedTask = new TaskEntity(UUID.randomUUID().toString(), "test", false, "author");
    when(taskService.getTaskById(any())).thenReturn(expectedTask);
    doNothing().when(taskService).deleteTask(any());
    UserDetails userDetails = getUserDetails("author");
    taskController.deleteTask(userDetails, "1");

    verify(taskService).deleteTask("1");
  }

  @Test
  void testDeleteTask_whenTaskDoesNotExist() throws TaskNotFoundException {
    when(taskService.getTaskById(any())).thenThrow(new TaskNotFoundException("Task not found"));
    doNothing().when(taskService).deleteTask(any());
    UserDetails userDetails = getUserDetails("author");

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> taskController.deleteTask(userDetails,"1"));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("404 NOT_FOUND \"Task not found\"", exception.getMessage());
  }

  @Test
  void testDeleteTask_whenUserIsNotTheAuthor() throws TaskNotFoundException {
    TaskEntity expectedTask = new TaskEntity(UUID.randomUUID().toString(), "test", false, "author");
    when(taskService.getTaskById(any())).thenReturn(expectedTask);
    UserDetails userDetails = getUserDetails("other");
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> taskController.deleteTask(userDetails, "1"));

    assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    assertEquals("403 FORBIDDEN \"User is not the author\"", exception.getMessage());
  }
}
