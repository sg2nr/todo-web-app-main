package ch.cern.todo.controller;

import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.exception.OperationNotPossibleException;
import ch.cern.todo.model.Category;
import ch.cern.todo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("categories"))
public class CategoryController {

  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public List<Category> getCategories() {
    return categoryService.getAllCategories();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Category addNewCategory(@RequestBody Category category) throws BadInputException {
    return categoryService.addNewCategory(category);
  }

  @DeleteMapping("/{categoryName}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable String categoryName)
      throws OperationNotPossibleException, BadInputException {
    categoryService.deleteCategory(categoryName);
  }

  @PatchMapping("/{categoryName}")
  public Category updateCategory(
      @PathVariable String categoryName, @RequestBody Category updatedCategory) throws BadInputException {
    return categoryService.updateCategory(categoryName, updatedCategory);
  }
}
