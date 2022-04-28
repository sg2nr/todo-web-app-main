package ch.cern.todo.controller;

import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskQueryCriteria;
import ch.cern.todo.service.TaskService;
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
  public List<Task> getTasks(@RequestBody TaskQueryCriteria taskQueryCriteria) {
    return taskService.getTasks(taskQueryCriteria);
  }

  @GetMapping("/{taskId}")
  public Task getTask(@PathVariable String taskId) throws BadInputException {
    return taskService.getTask(taskId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Task addNewTask(@RequestBody Task task) throws BadInputException {
    return taskService.addNewTask(task);
  }

  @DeleteMapping("/{taskId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(@PathVariable String taskId) throws BadInputException {
    taskService.deleteTask(taskId);
  }

  @PatchMapping("/{taskId}")
  public Task updateTask(@PathVariable String taskId, @RequestBody Task updatedTask) throws BadInputException {
    return taskService.updateTask(taskId, updatedTask);
  }
}
