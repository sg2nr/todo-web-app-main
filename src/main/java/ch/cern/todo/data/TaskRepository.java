package ch.cern.todo.data;

import ch.cern.todo.data.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

  @Query(
      value =
          "SELECT t FROM TaskEntity t, TaskCategoryEntity c "
              + "WHERE t.category=c "
              + "AND c.name in :categoryNames "
              + "ORDER BY t.deadline ASC")
  List<TaskEntity> getTaskEntitiesByCategory(@Param("categoryNames") List<String> categoryNames);

  @Query(
      value =
          "SELECT t FROM TaskEntity t, TaskCategoryEntity c "
              + "WHERE t.category=c "
              + "AND t.deadline < ?1 "
              + "ORDER BY t.deadline ASC")
  List<TaskEntity> getTaskEntitiesBeforeDeadline(ZonedDateTime beforeDeadline);

  @Query(
      value =
          "SELECT t FROM TaskEntity t, TaskCategoryEntity c "
              + "WHERE t.category=c "
              + "AND c.name in :categoryNames "
              + "AND t.deadline < :beforeDeadline "
              + "ORDER BY t.deadline ASC")
  List<TaskEntity> getTaskEntitiesByCategoryAndBeforeDeadline(
      @Param("categoryNames") List<String> categoryNames,
      @Param("beforeDeadline") ZonedDateTime beforeDeadline);
}
