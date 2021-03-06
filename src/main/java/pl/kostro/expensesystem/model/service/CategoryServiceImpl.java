package pl.kostro.expensesystem.model.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class CategoryServiceImpl implements CategoryService {

  private CategoryRepository repository;

  private Logger logger = LogManager.getLogger();

  @Autowired
  public CategoryServiceImpl(CategoryRepository repository) {
    this.repository = repository;
  }
  
  public void save(CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.save(category);
    logger.info("save for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void remove(CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.delete(category);
    logger.info("removeCategory for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  void decrypt(CategoryEntity category) {
    category.getName();
  }

  void encrypt(CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    category.setName(category.getName(true), true);
    repository.save(category);
    logger.info("encrypt for {} finish: {} ms", category, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

}
