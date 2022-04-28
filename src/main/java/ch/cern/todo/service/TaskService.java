package ch.cern.todo.service;

import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskQueryCriteria;

import java.util.List;

public interface TaskService {

  Task getTask(String taskId) throws BadInputException;

  List<Task> getTasks(TaskQueryCriteria taskQueryCriteria);

  Task addNewTask(Task task) throws BadInputException;

  void deleteTask(String taskId) throws BadInputException;

  Task updateTask(String taskId, Task updatedTask) throws BadInputException;
}
