package com.myKeyCons.tasks.domain.entity;


public class TaskEntity {
  private String id;
  private String label;
  private Boolean complete;
  private String author;

  public TaskEntity(String id, String label, Boolean complete, String author) {
    this.id = id;
    this.label = label;
    this.complete = complete;
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

  public Boolean getComplete() {
    return complete;
  }

  public void complete() {
    this.complete = true;
  }

  public void unComplete(){ this.complete = false;}

  public String getAuthor() {
    return author;
  }

}
