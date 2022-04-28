package ch.cern.todo.service;

import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.exception.OperationNotPossibleException;
import ch.cern.todo.model.Category;

import java.util.List;

public interface CategoryService {

  List<Category> getAllCategories();

  Category addNewCategory(Category category) throws BadInputException;

  void deleteCategory(String categoryName) throws OperationNotPossibleException, BadInputException;
}
