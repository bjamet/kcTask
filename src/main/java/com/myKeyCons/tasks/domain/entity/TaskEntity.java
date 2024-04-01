package com.myKeyCons.tasks.domain.entity;


public class TaskEntity {
  private String id;
  private String label;
  private String description;
  private String author;

  public TaskEntity(String id, String label, String description, String author) {
    this.id = id;
    this.label = label;
    this.description = description;
    this.author = author;
  }
  public String getId() {
    return id;
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
}
