package ch.cern.todo.service;

import ch.cern.todo.data.TaskCategoryRepository;
import ch.cern.todo.data.entity.TaskCategory;
import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.model.Category;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesManager implements CategoryService {

  private final TaskCategoryRepository repository;

  @Autowired
  public CategoriesManager(TaskCategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Category> getAllCategories() {
    return repository.findAll().stream()
        .map(
            taskCategory -> {
              Category category = new Category();
              category.setName(taskCategory.getName());
              category.setDescription(taskCategory.getDescription());

              return category;
            })
        .collect(Collectors.toList());
  }

  @Override
  public Category addNewCategory(Category category) {

    if (StringUtils.isBlank(category.getName())) {
      throw new BadInputException("Missing name for a new category.");
    }

    if (repository.findByName(category.getName()).isPresent()) {
      throw new BadInputException("A category exists already with the name in input.");
    }

    TaskCategory taskCategoryToPersist = new TaskCategory();
    taskCategoryToPersist.setName(category.getName());
    taskCategoryToPersist.setDescription(category.getDescription());

    TaskCategory persistedTaskCategory = repository.save(taskCategoryToPersist);

    Category newCategory = new Category();
    newCategory.setName(persistedTaskCategory.getName());
    newCategory.setDescription(persistedTaskCategory.getDescription());

    return newCategory;
  }
}
