package com.myKeyCons.tasks.dto;

import com.myKeyCons.tasks.domain.entity.TaskEntity;

public class TaskDTO {
    private String id;

  public TaskDTO(String id, String label, String description, String author) {
    this.id = id;
    this.label = label;
    this.description = description;
    this.author = author;
  }

  private String label;
    private String description;
    private String author;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getAuthor() {
      return author;
    }

    public void setAuthor(String author) {
      this.author = author;
    }

  public static TaskDTO fromTaskEntity(TaskEntity entity) {
    return new TaskDTO(entity.getId(), entity.getLabel(), entity.getDescription(), entity.getAuthor());
  }

}
