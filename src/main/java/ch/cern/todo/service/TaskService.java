package ch.cern.todo.service;

import ch.cern.todo.model.Task;

import java.util.List;

public interface TaskService {

  List<Task> getTasks();

  List<Task> getTasksByCategory(String categoryName);

  Task addNewTask(Task task);
}
