package ch.cern.todo.service.impl;

import ch.cern.todo.data.TaskCategoryRepository;
import ch.cern.todo.data.TaskRepository;
import ch.cern.todo.data.entity.TaskCategoryEntity;
import ch.cern.todo.data.entity.TaskEntity;
import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskQueryCriteria;
import ch.cern.todo.service.TaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
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
  public Task getTask(String taskId) throws BadInputException {
    TaskEntity taskFromDataBase = getTaskEntityById(taskId);
    return mapTask(taskFromDataBase);
  }

  @Override
  public List<Task> getTasks(TaskQueryCriteria taskQueryCriteria) {
    ZonedDateTime beforeDate = taskQueryCriteria.getBeforeDeadline();
    List<TaskEntity> taskEntities =
        retrieveTaskEntities(taskQueryCriteria.getCategories(), beforeDate);

    return taskEntities.stream().map(this::mapTask).collect(Collectors.toList());
  }

  public List<TaskEntity> retrieveTaskEntities(
      List<String> categoryNames, ZonedDateTime beforeDate) {
    if (CollectionUtils.isNotEmpty(categoryNames) && beforeDate != null) {
      return taskRepository.getTaskEntitiesByCategoryAndBeforeDeadline(categoryNames, beforeDate);
    }

    if (CollectionUtils.isNotEmpty(categoryNames)) {
      return taskRepository.getTaskEntitiesByCategory(categoryNames);
    }

    if (beforeDate != null) {
      return taskRepository.getTaskEntitiesBeforeDeadline(beforeDate);
    }

    return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "deadline"));
  }

  @Override
  public Task addNewTask(Task task) throws BadInputException {
    if (StringUtils.isBlank(task.getName())) {
      throw new BadInputException("Missing task name.");
    }

    if (task.getDeadline() == null) {
      throw new BadInputException("Missing task deadline.");
    }

    if (StringUtils.isBlank(task.getCategory())) {
      throw new BadInputException("Missing task category.");
    }

    TaskEntity taskToPersist = mapTaskEntity(task);

    TaskEntity persistedTask = taskRepository.save(taskToPersist);

    return mapTask(persistedTask);
  }

  private TaskEntity mapTaskEntity(Task task) throws BadInputException {
    TaskCategoryEntity correspondingCategory =
        taskCategoryRepository
            .findByName(task.getCategory())
            .orElseThrow(() -> new BadInputException("The provided category does not exist."));

    TaskEntity taskToPersist = new TaskEntity();
    taskToPersist.setName(task.getName());
    taskToPersist.setDescription(task.getDescription());
    taskToPersist.setDeadline(task.getDeadline());
    taskToPersist.setCategory(correspondingCategory);
    return taskToPersist;
  }

  public void deleteTask(String taskId) throws BadInputException {
    TaskEntity taskFromDataBase = getTaskEntityById(taskId);
    taskRepository.delete(taskFromDataBase);
  }

  @Override
  public Task updateTask(String taskId, Task updatedTask) throws BadInputException {

    TaskEntity taskEntityById = getTaskEntityById(taskId);

    String newName = updatedTask.getName();
    if (StringUtils.isNotBlank(newName)) {
      taskEntityById.setName(newName);
    }

    String newDescription = updatedTask.getDescription();
    if (StringUtils.isNotBlank(newDescription)) {
      taskEntityById.setDescription(newDescription);
    }

    ZonedDateTime newDeadline = updatedTask.getDeadline();
    if (newDeadline != null) {
      taskEntityById.setDeadline(newDeadline);
    }

    TaskEntity persistedTaskEntity = taskRepository.save(taskEntityById);
    return mapTask(persistedTaskEntity);
  }

  private TaskEntity getTaskEntityById(String taskId) throws BadInputException {
    if (StringUtils.isNumeric(taskId)) {
      long numericTaskId = Long.parseLong(taskId);
      return taskRepository
          .findById(numericTaskId)
          .orElseThrow(
              () ->
                  new BadInputException(String.format("Task with id %s does not exist.", taskId)));
    } else {
      throw new BadInputException("Invalid taskId. TaskId must be a numeric value.");
    }
  }

  private Task mapTask(TaskEntity taskEntity) {
    Task task = new Task();
    task.setId(String.valueOf(taskEntity.getId()));
    task.setName(taskEntity.getName());
    task.setDescription(taskEntity.getDescription());
    task.setDeadline(taskEntity.getDeadline());
    task.setCategory(taskEntity.getCategory().getName());
    return task;
  }
}
