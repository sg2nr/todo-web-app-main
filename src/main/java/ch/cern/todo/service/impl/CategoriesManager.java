package ch.cern.todo.service.impl;

import ch.cern.todo.data.TaskCategoryRepository;
import ch.cern.todo.data.entity.TaskCategoryEntity;
import ch.cern.todo.exception.BadInputException;
import ch.cern.todo.exception.OperationNotPossibleException;
import ch.cern.todo.model.Category;
import ch.cern.todo.service.CategoryService;
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
            taskCategoryEntity -> {
              Category category = new Category();
              category.setName(taskCategoryEntity.getName());
              category.setDescription(taskCategoryEntity.getDescription());

              return category;
            })
        .collect(Collectors.toList());
  }

  @Override
  public Category addNewCategory(Category category) throws BadInputException {

    if (StringUtils.isBlank(category.getName())) {
      throw new BadInputException("Missing name for a new category.");
    }

    if (repository.findByName(category.getName()).isPresent()) {
      throw new BadInputException("A category exists already with the name in input.");
    }

    TaskCategoryEntity taskCategoryEntityToPersist = new TaskCategoryEntity();
    taskCategoryEntityToPersist.setName(category.getName());
    taskCategoryEntityToPersist.setDescription(category.getDescription());

    TaskCategoryEntity persistedTaskCategoryEntity = repository.save(taskCategoryEntityToPersist);

    Category newCategory = new Category();
    newCategory.setName(persistedTaskCategoryEntity.getName());
    newCategory.setDescription(persistedTaskCategoryEntity.getDescription());

    return newCategory;
  }

  @Override
  public void deleteCategory(String categoryName) throws OperationNotPossibleException, BadInputException {
    TaskCategoryEntity taskCategoryEntity =
        repository
            .findByName(categoryName)
            .orElseThrow(
                () ->
                    new BadInputException(
                        String.format("Category with name %s does not exist.", categoryName)));

    if (isNotDeletable(categoryName)) {
      throw new OperationNotPossibleException(
          String.format(
              "Impossible to delete category with name %s, since there are still tasks associated to it.",
              categoryName));
    }

    repository.delete(taskCategoryEntity);
  }

  private boolean isNotDeletable(String categoryName) {
    return repository.findTaskCategoryEntityWithoutTasks(categoryName).isEmpty();
  }
}
