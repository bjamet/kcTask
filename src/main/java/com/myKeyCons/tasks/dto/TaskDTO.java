package com.myKeyCons.tasks.dto;

import com.myKeyCons.tasks.domain.entity.TaskEntity;

public class TaskDTO {
    private String id;

  public TaskDTO(String id, String label, Boolean complete, String author) {
    this.id = id;
    this.label = label;
    this.complete = complete;
    this.author = author;
  }

  private String label;
    private Boolean complete;
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

    public Boolean getComplete() {
      return complete;
    }

    public void setComplete(Boolean complete) {
      this.complete = complete;
    }

    public String getAuthor() {
      return author;
    }

    public void setAuthor(String author) {
      this.author = author;
    }

  public static TaskDTO fromTaskEntity(TaskEntity entity) {
    return new TaskDTO(entity.getId(), entity.getLabel(), entity.getComplete(), entity.getAuthor());
  }

}
