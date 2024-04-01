package com.myKeyCons.tasks.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
class TaskDTOTest {

  @Test
  void testFromTaskEntity() throws Exception {
    UUID taskId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    String label = "task1";
    Boolean complete = false;
    String author = "author";

    TaskEntity taskEntity = new TaskEntity(taskId.toString(), label, complete, author);

    TaskDTO actualTaskDTO = TaskDTO.fromTaskEntity(taskEntity);
    assertEquals(actualTaskDTO.getId(), taskId.toString());
    assertEquals(actualTaskDTO.getLabel(), label);
    assertEquals(actualTaskDTO.getComplete(), complete);
    assertEquals(actualTaskDTO.getAuthor(), author);
  }
}
