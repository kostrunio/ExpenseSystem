package pl.kostro.expensesystem.model.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository repository;

  @Autowired
  public CategoryServiceImpl(CategoryRepository repository) {
    this.repository = repository;
  }
  
  public void save(CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.save(category);
    log.info("save for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void remove(CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.delete(category);
    log.info("removeCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  void decrypt(CategoryEntity category) {
    category.getName();
  }

  void encrypt(CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    category.setName(category.getName(true), true);
    repository.save(category);
    log.info("encrypt for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

}
