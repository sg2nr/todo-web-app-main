package ch.cern.todo.data;

import ch.cern.todo.data.entity.TaskCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategoryEntity, Long> {

  Optional<TaskCategoryEntity> findByName(String name);

  @Query(
      value =
          "SELECT c FROM TaskCategoryEntity c "
              + "LEFT JOIN TaskEntity t "
              + "ON c=t.category "
              + "WHERE c.name = :name "
              + "AND t.id IS NULL")
  Optional<TaskCategoryEntity> findTaskCategoryEntityWithoutTasks(String name);
}
