package ch.cern.todo.controller;

import ch.cern.todo.model.Category;
import ch.cern.todo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
  public Category addNewCategory(@RequestBody Category category) {
    return categoryService.addNewCategory(category);
  }
}