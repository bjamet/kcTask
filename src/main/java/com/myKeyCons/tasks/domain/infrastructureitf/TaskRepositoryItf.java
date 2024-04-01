package com.myKeyCons.tasks.domain.infrastructureitf;

import com.myKeyCons.tasks.domain.entity.TaskEntity;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryItf {
  public List<TaskEntity> findAll() ;

  public void save(TaskEntity entity) ;

  public Optional<TaskEntity> findById(String id) ;

  public Optional<TaskEntity> deleteById(String id) ;
}
