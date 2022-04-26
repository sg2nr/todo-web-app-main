package ch.cern.todo.controller;

import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {

  private final TaskService taskService;

  @Autowired
  public TodoController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("tasks")
  public List<Task> getTasksByCategory(
      @RequestParam(name = "category", required = false) String categoryName) {
    if (StringUtils.isBlank(categoryName)) {
      return taskService.getTasks();
    } else {
      return taskService.getTasksByCategory(categoryName);
    }
  }

  @PostMapping("tasks")
  public Task addNewTask(@RequestBody Task task) {
    return taskService.addNewTask(task);
  }
}
