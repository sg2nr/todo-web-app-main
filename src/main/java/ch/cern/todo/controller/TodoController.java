package ch.cern.todo.controller;

import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("tasks"))
public class TodoController {

  private final TaskService taskService;

  @Autowired
  public TodoController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<Task> getTasks(
      @RequestParam(name = "category", required = false) String categoryName) {
    if (StringUtils.isBlank(categoryName)) {
      return taskService.getTasks();
    } else {
      return taskService.getTasksByCategory(categoryName);
    }
  }

  @GetMapping("/{taskId}")
  public Task getTask(@PathVariable String taskId) {
    return taskService.getTask(taskId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Task addNewTask(@RequestBody Task task) {
    return taskService.addNewTask(task);
  }

  @DeleteMapping("/{taskId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(@PathVariable String taskId) {
    taskService.deleteTask(taskId);
  }
}
