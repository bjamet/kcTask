package com.myKeyCons.tasks.repository;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import com.myKeyCons.tasks.domain.infrastructureitf.TaskRepositoryItf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository implements TaskRepositoryItf {

  private Map<String, TaskEntity> tasks = new HashMap<>();


  @Override
  public List<TaskEntity> findAll() {
    return tasks.values().stream().sorted((t1,t2) -> {
      return t1.getLabel().compareToIgnoreCase(t2.getLabel());
    }).collect(Collectors.toList());
  }

  @Override
  public void save(TaskEntity entity) {
    tasks.put(entity.getId(), entity);
  }

  @Override
  public Optional<TaskEntity> findById(String id) {
    TaskEntity task = tasks.get(id);
    if (task == null){
      return Optional.empty();
    }
    return Optional.of(task);
  }

  @Override
  public Optional<TaskEntity> deleteById(String id) {
    Optional<TaskEntity> taskOpt = this.findById(id);
    if (taskOpt.isPresent()){
      tasks.remove(id);
    }
    return taskOpt;
  }
}
