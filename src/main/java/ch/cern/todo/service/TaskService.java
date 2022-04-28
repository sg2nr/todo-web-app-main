package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskQueryCriteria;

import java.util.List;

public interface TaskService {

  Task getTask(String taskId);

  List<Task> getTasks(TaskQueryCriteria taskQueryCriteria);

  Task addNewTask(Task task);

  void deleteTask(String taskId);
}
