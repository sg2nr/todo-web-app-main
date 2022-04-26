package ch.cern.todo.data;

import ch.cern.todo.data.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {

  Optional<TaskCategory> findByName(String name);
}
