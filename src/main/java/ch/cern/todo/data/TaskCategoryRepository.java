package ch.cern.todo.data;

import ch.cern.todo.data.entity.TaskCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategoryEntity, Long> {

  Optional<TaskCategoryEntity> findByName(String name);
}
