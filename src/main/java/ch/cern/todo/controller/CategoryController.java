package ch.cern.todo.controller;

import ch.cern.todo.model.Category;
import ch.cern.todo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/categories")
  public List<Category> getCategories() {
    return categoryService.getAllCategories();
  }

  @PostMapping("/categories")
  @ResponseStatus(HttpStatus.CREATED)
  public Category addNewCategory(@RequestBody Category category) {
    return categoryService.addNewCategory(category);
  }
}
