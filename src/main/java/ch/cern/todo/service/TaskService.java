package ch.cern.todo.service;

import ch.cern.todo.model.Task;

import java.util.List;

public interface TaskService {

  List<Task> getTasks();

  List<Task> getTasksByCategory(String categoryName);

  Task getTask(String taskId);

  Task addNewTask(Task task);

  void deleteTask(String taskId);
}
