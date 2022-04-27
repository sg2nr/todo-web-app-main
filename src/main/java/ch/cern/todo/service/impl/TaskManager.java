package ch.cern.todo.service.impl;

import ch.cern.todo.data.TaskCategoryRepository;
import ch.cern.todo.data.TaskRepository;
import ch.cern.todo.data.entity.TaskCategoryEntity;
import ch.cern.todo.data.entity.TaskEntity;
import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManager implements TaskService {

  private final TaskRepository taskRepository;

  private final TaskCategoryRepository taskCategoryRepository;

  @Autowired
  public TaskManager(TaskRepository taskRepository, TaskCategoryRepository taskCategoryRepository) {
    this.taskRepository = taskRepository;
    this.taskCategoryRepository = taskCategoryRepository;
  }

  @Override
  public List<Task> getTasks() {
    List<TaskEntity> taskEntities = taskRepository.findAll(Sort.by(Sort.Direction.ASC, "deadline"));

    return taskEntities.stream().map(this::mapTask).collect(Collectors.toList());
  }

  @Override
  public List<Task> getTasksByCategory(String categoryName) {
    if (StringUtils.isBlank(categoryName)) {
      throw new BadInputException("Missing category name.");
    }

    List<TaskEntity> taskEntitiesByCategory =
        taskRepository.getTaskEntitiesByCategory(categoryName);

    return taskEntitiesByCategory.stream().map(this::mapTask).collect(Collectors.toList());
  }

  @Override
  public Task addNewTask(Task task) {

    if (StringUtils.isBlank(task.getName())) {
      throw new BadInputException("Missing task name.");
    }

    if (task.getDeadline() == null) {
      throw new BadInputException("Missing task deadline.");
    }

    if (StringUtils.isBlank(task.getCategory())) {
      throw new BadInputException("Missing task category.");
    }

    TaskCategoryEntity correspondingCategory =
        taskCategoryRepository
            .findByName(task.getCategory())
            .orElseThrow(() -> new BadInputException("The provided category does not exist."));

    TaskEntity taskToPersist = new TaskEntity();
    taskToPersist.setName(task.getName());
    taskToPersist.setDescription(task.getDescription());
    taskToPersist.setDeadline(task.getDeadline());
    taskToPersist.setCategory(correspondingCategory);

    TaskEntity persistedTask = taskRepository.save(taskToPersist);

    return mapTask(persistedTask);
  }

  private Task mapTask(TaskEntity taskEntity) {
    Task task = new Task();
    task.setName(taskEntity.getName());
    task.setDescription(taskEntity.getDescription());
    task.setDeadline(taskEntity.getDeadline());
    task.setCategory(taskEntity.getCategory().getName());
    return task;
  }
}
