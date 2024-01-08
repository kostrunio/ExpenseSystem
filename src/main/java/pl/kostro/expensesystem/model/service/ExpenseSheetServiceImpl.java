package pl.kostro.expensesystem.model.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;
import pl.kostro.expensesystem.model.repository.ExpenseSheetRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class ExpenseSheetServiceImpl implements ExpenseSheetService {

  private final ExpenseSheetRepository repository;

  @Autowired
  private CategoryServiceImpl cs;
  @Autowired
  private ExpenseServiceImpl es;
  @Autowired
  private UserLimitServiceImpl uls;
  @Autowired
  private UserSummaryServiceImpl uss;

  @Autowired
  public ExpenseSheetServiceImpl(ExpenseSheetRepository repository) {
    this.repository = repository;
  }

  public ExpenseSheetEntity create(String name, String key, RealUserEntity owner, UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    ExpenseSheetEntity expenseSheet = new ExpenseSheetEntity();
    expenseSheet.setName(name);
    expenseSheet.setEncrypted(true);
    expenseSheet.setSecretKey(key);
    expenseSheet.setOwner(owner);
    expenseSheet.getUserLimitList().add(userLimit);
    repository.save(expenseSheet);
    log.info("createExpenseSheet for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public void merge(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.save(expenseSheet);
    log.info("merge for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeExpenseSheet(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    repository.delete(expenseSheet);
    log.info("removeExpenseSheet for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void decrypt(ExpenseSheetEntity expenseSheet) {
    log.info("decrypt: category");
    for (CategoryEntity category : expenseSheet.getCategoryList()) {
      cs.decrypt(category);
    }
    log.info("decrypt: expense");
    for (ExpenseEntity expense : expenseSheet.getExpenseList()) {
      es.decrypt(expense);
    }
    log.info("decrypt: userLimit");
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList()) {
      uls.decrypt(userLimit);
      for (UserSummaryEntity userSummary : userLimit.getUserSummaryList()) {
        uss.decrypt(userSummary);
      }
    }
  }

  public void encrypt(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    log.info("encrypt: category");
    for (CategoryEntity category : expenseSheet.getCategoryList()) {
      cs.encrypt(category);
    }
    log.info("encrypt: expense");
    for (ExpenseEntity expense : expenseSheet.getExpenseList()) {
      es.encrypt(expense);
    }
    log.info("encrypt: userLimit");
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList()) {
      uls.encrypt(userLimit);
      for (UserSummaryEntity userSummary : userLimit.getUserSummaryList()) {
        uss.encrypt(userSummary);
      }
    }
    log.info("encrypt for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public ExpenseSheetEntity moveCategoryUp(ExpenseSheetEntity expenseSheet, CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    if (category.getOrder() == 0)
      return expenseSheet;
    category.setOrder(category.getOrder() - 1);
    for (CategoryEntity cat : expenseSheet.getCategoryList()) {
      if (cat.getOrder() == category.getOrder())
        if (!cat.equals(category))
          cat.setOrder(cat.getOrder() + 1);
    }
    CategoryEntity tmp = expenseSheet.getCategoryList().get(category.getOrder());
    expenseSheet.getCategoryList().set(category.getOrder() + 1, tmp);
    expenseSheet.getCategoryList().set(category.getOrder(), category);

    merge(expenseSheet);
    log.info("moveCategoryUp for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public ExpenseSheetEntity moveCategoryDown(ExpenseSheetEntity expenseSheet, CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    if (category.getOrder() == expenseSheet.getCategoryList().size())
      return expenseSheet;
    category.setOrder(category.getOrder() + 1);
    for (CategoryEntity cat : expenseSheet.getCategoryList()) {
      if (cat.getOrder() == category.getOrder())
        if (!cat.equals(category))
          cat.setOrder(cat.getOrder() - 1);
    }
    CategoryEntity tmp = expenseSheet.getCategoryList().get(category.getOrder());
    expenseSheet.getCategoryList().set(category.getOrder() - 1, tmp);
    expenseSheet.getCategoryList().set(category.getOrder(), category);

    merge(expenseSheet);
    log.info("moveCategoryDown for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  @Transactional
  public void fetchCategoryList(ExpenseSheetEntity expenseSheet) {
    try {
      expenseSheet.getCategoryList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      ExpenseSheetEntity attached = repository.getOne(expenseSheet.getId());
      attached.getCategoryList().size();
      expenseSheet.setCategoryList(attached.getCategoryList());
      log.info("fetchCategoryList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  @Transactional
  public void fetchExpenseList(ExpenseSheetEntity expenseSheet) {
    try {
      expenseSheet.getExpenseList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      ExpenseSheetEntity attached = repository.getOne(expenseSheet.getId());
      attached.getExpenseList().size();
      expenseSheet.setExpenseList(attached.getExpenseList());
      log.info("fetchExpenseList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  @Transactional
  public void fetchUserLimitList(ExpenseSheetEntity expenseSheet) {
    try {
      expenseSheet.getUserLimitList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      ExpenseSheetEntity attached = repository.getOne(expenseSheet.getId());
      attached.getUserLimitList().size();
      expenseSheet.setUserLimitList(attached.getUserLimitList());
      log.info("fetchUserLimitList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public void addCategory(CategoryEntity category, ExpenseSheetEntity expenseSheet) {
	expenseSheet.getCategoryList().add(category);
    repository.save(expenseSheet);
  }

  public void removeCategory(CategoryEntity category, ExpenseSheetEntity expenseSheet) {
	expenseSheet.getCategoryList().remove(category);
    int i = 0;
    for (CategoryEntity cat : expenseSheet.getCategoryList()) {
      cat.setOrder(i++);
    }
    repository.save(expenseSheet);
  }
}
