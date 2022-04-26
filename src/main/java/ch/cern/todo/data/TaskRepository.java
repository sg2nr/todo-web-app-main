package ch.cern.todo.data;

import ch.cern.todo.data.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

  @Query(
      value =
          "SELECT t FROM TaskEntity t, TaskCategoryEntity c WHERE t.category=c AND c.name=?1 ORDER BY t.deadline ASC")
  List<TaskEntity> getTaskEntitiesByCategory(String categoryName);
}
