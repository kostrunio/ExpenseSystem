package pl.kostro.expensesystem.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.entity.*;
import pl.kostro.expensesystem.model.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.model.repository.UserLimitRepository;
import pl.kostro.expensesystem.ui.notification.ShowNotification;
import pl.kostro.expensesystem.utils.filter.Filter;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;
import pl.kostro.expensesystem.utils.expense.YearCategory;

@Service
public class ExpenseSheetServiceImpl implements ExpenseSheetService {
  
  @Autowired
  private ExpenseSheetRepository eshr;

  @Autowired
  private UserLimitRepository ulr;
  @Autowired
  private CategoryServiceImpl cs;
  @Autowired
  private ExpenseService es;
  @Autowired
  private RealUserService rus;
  @Autowired
  private UserLimitService uls;
  @Autowired
  private UserSummaryService uss;

  private Logger logger = LogManager.getLogger();

  public ExpenseSheetEntity create(String name, String key, RealUserEntity owner, UserLimitEntity userLimit) {
    LocalDateTime stopper = LocalDateTime.now();
    ExpenseSheetEntity expenseSheet = new ExpenseSheetEntity();
    expenseSheet.setName(name);
    expenseSheet.setEncrypted(true);
    expenseSheet.setSecretKey(key);
    expenseSheet.setOwner(owner);
    expenseSheet.getUserLimitList().add(userLimit);
    eshr.save(expenseSheet);
    logger.info("createExpenseSheet for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public void merge(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    eshr.save(expenseSheet);
    logger.info("merge for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeExpenseSheet(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    eshr.delete(expenseSheet);
    logger.info("removeExpenseSheet for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void decrypt(ExpenseSheetEntity expenseSheet) {
    logger.info("decrypt: category");
    for (CategoryEntity category : expenseSheet.getCategoryList())
      cs.decrypt(category);
    logger.info("decrypt: expense");
    for (ExpenseEntity expense : expenseSheet.getExpenseList())
      es.decrypt(expense);
    logger.info("decrypt: userLimit");
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList())
      uls.decrypt(userLimit);
  }

  public void encrypt(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    logger.info("encrypt: category");
    for (CategoryEntity category : expenseSheet.getCategoryList())
      cs.encrypt(category);
    logger.info("encrypt: expense");
    for (ExpenseEntity expense : expenseSheet.getExpenseList())
      es.encrypt(expense);
    logger.info("encrypt: userLimit");
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList())
      uls.encrypt(userLimit);
    logger.info("encrypt for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public ExpenseSheetEntity findExpenseSheet(RealUserEntity realUser, int id) {
    Optional<ExpenseSheetEntity> result = realUser.getExpenseSheetList().parallelStream()
      .filter(esh -> esh.getId() == id)
      .findFirst();
    if (result.isPresent())
      return result.get();
    return realUser.getDefaultExpenseSheet();
  }

  public List<ExpenseEntity> findAllExpense(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<ExpenseEntity> expenseListToReturn = expenseSheet.getExpenseList().parallelStream()
        .filter(e -> Filter.matchFilter(e, expenseSheet.getFilter()))
        .collect(Collectors.toList());
    logger.info("findAllExpense for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseListToReturn;
  }

  private List<ExpenseEntity> getExpenseList(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<ExpenseEntity> expenseListToReturn = expenseSheet.getExpenseList().parallelStream()
    .filter(e -> !e.getDate().isBefore(expenseSheet.getFirstDate()))
    .filter(e -> !e.getDate().isAfter(expenseSheet.getLastDate()))
    .filter(e -> Filter.matchFilter(e, expenseSheet.getFilter()))
    .collect(Collectors.toList());
    logger.info("getExpenseList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseListToReturn;
  }

  public Map<LocalDate, DateExpense> prepareExpenseMap(ExpenseSheetEntity expenseSheet, LocalDate startDate, LocalDate endDate,
                                                       LocalDate firstDay, LocalDate lastDay) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getDateExpenseMap().clear();
    expenseSheet.getCategoryExpenseMap().clear();
    expenseSheet.getUserLimitExpenseMap().clear();
    expenseSheet.setFirstDate(startDate);
    expenseSheet.setLastDate(endDate);
    for (ExpenseEntity expense : getExpenseList(expenseSheet)) {
      if (expense.getValue() == null) continue;
      addExpenseToDateMap(expenseSheet, expense);
      if (firstDay != null && lastDay != null && !expense.getDate().isBefore(firstDay)
          && !expense.getDate().isAfter(lastDay)) {
        addExpenseToCategoryMap(expenseSheet, expense);
        addExpenseToUserLimitMap(expenseSheet, expense);
      }
    }
    logger.info("prepareExpenseMap for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet.getDateExpenseMap();
  }

  public void addExpense(ExpenseEntity expense, ExpenseSheetEntity expenseSheet) {
    expenseSheet.getExpenseList().add(expense);
    addExpenseToDateMap(expenseSheet, expense);
  }

  private void addExpenseToDateMap(ExpenseSheetEntity expenseSheet, ExpenseEntity expense) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    if (dateExpense == null) {
      dateExpense = new DateExpense(expense.getDate());
      expenseSheet.getDateExpenseMap().put(expense.getDate(), dateExpense);
    }
    dateExpense.addExpense(expenseSheet, expense);
  }

  private void addExpenseToCategoryMap(ExpenseSheetEntity expenseSheet, ExpenseEntity expense) {
    CategoryExpense categoryExpense = expenseSheet.getCategoryExpenseMap().get(expense.getCategory());
    if (categoryExpense == null) {
      categoryExpense = new CategoryExpense(expense.getCategory());
      expenseSheet.getCategoryExpenseMap().put(expense.getCategory(), categoryExpense);
    }
    categoryExpense.addExpense(expense);
  }

  private void addExpenseToUserLimitMap(ExpenseSheetEntity expenseSheet, ExpenseEntity expense) {
    UserLimitEntity userLimit = getUserLimitForUser(expenseSheet, expense.getUser());
    UserLimitExpense userLimitExpense = expenseSheet.getUserLimitExpenseMap().get(userLimit);
    if (userLimitExpense == null) {
      userLimitExpense = new UserLimitExpense(userLimit);
      expenseSheet.getUserLimitExpenseMap().put(userLimitExpense.getUserLimit(), userLimitExpense);
    }
    userLimitExpense.addExpense(expense);
  }

  public void removeExpense(ExpenseEntity expense, ExpenseSheetEntity expenseSheet) {
    expenseSheet.getExpenseList().remove(expense);
    removeExpenseFromMap(expenseSheet, expense);
  }

  private void removeExpenseFromMap(ExpenseSheetEntity expenseSheet, ExpenseEntity expense) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    if (dateExpense != null)
      dateExpense.removeExpense(expense);
  }

  public UserLimitEntity getUserLimitForUser(ExpenseSheetEntity expenseSheet, UserEntity user) {
    Optional<UserLimitEntity> result = expenseSheet.getUserLimitList().parallelStream()
      .filter(ul -> ul.getUser().equals(user))
      .findFirst();
    if (result.isPresent())
      return result.get();
    else
      return null;
  }

  public Set<String> getAllComments(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    Set<String> commentsList = expenseSheet.getExpenseList().stream()
        .filter(e -> e.getComment() != null && !e.getComment().isEmpty())
        .map(e -> e.getComment())
        .collect(Collectors.toSet());
    logger.info("getAllComments for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return commentsList;
  }

  public Set<String> getCommentForCategory(ExpenseSheetEntity expenseSheet, CategoryEntity category) {
    LocalDateTime stopper = LocalDateTime.now();
    Set<String> commentsList = expenseSheet.getExpenseList().stream()
        .filter(e -> e.getCategory().equals(category))
        .filter(e -> e.getComment() != null && !e.getComment().isEmpty())
        .map(e -> e.getComment())
        .collect(Collectors.toSet());
    logger.info("getCommentForCategory for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return commentsList;
  }

  public List<String> getYearList(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<String> yearList = new ArrayList<>();
    int year = LocalDate.now().getYear();
    int limitYear = year;
    OptionalInt maxYear = expenseSheet.getExpenseList().parallelStream()
            .mapToInt(e -> e.getDate().getYear())
            .max();
    OptionalInt expYear = expenseSheet.getExpenseList().parallelStream()
      .mapToInt(e -> e.getDate().getYear())
      .min();
    if (expYear.isPresent()) year = expYear.getAsInt();
    if (maxYear.isPresent()) limitYear = maxYear.getAsInt();
    for (int i = year; i <= limitYear; i++)
      yearList.add(Integer.toString(i));
    logger.info("getYearList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return yearList;
  }

  public DateExpense getDateExpenseMap(ExpenseSheetEntity expenseSheet, LocalDate date) {
    if (date.isBefore(expenseSheet.getFirstDate()) || date.isAfter(expenseSheet.getLastDate())) {
      prepareExpenseMap(expenseSheet, date.withDayOfMonth(1), date.withDayOfMonth(date.lengthOfMonth()), null, null);
    }
    return expenseSheet.getDateExpenseMap().get(date);
  }

  public CategoryExpense getCategoryExpenseMap(ExpenseSheetEntity expenseSheet, CategoryEntity category) {
    return expenseSheet.getCategoryExpenseMap().get(category);
  }

  public UserLimitExpense getUserLimitExpenseMap(ExpenseSheetEntity expenseSheet, UserLimitEntity userLimit) {
    return expenseSheet.getUserLimitExpenseMap().get(userLimit);
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
    logger.info("moveCategoryUp for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
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
    logger.info("moveCategoryDown for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public List<YearCategory> prepareYearCategoryList(ExpenseSheetEntity expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<YearCategory> yearCategoryList = new ArrayList<YearCategory>();
    LocalDate firstDay = LocalDate.now();
    for (String year : getYearList(expenseSheet)) {
      YearCategory yearCategory = new YearCategory(Integer.parseInt(year), expenseSheet.getCategoryList());
      firstDay = firstDay.withYear(Integer.parseInt(year)).withDayOfMonth(1);
      for (int m = 1; m <= 12; m++) {
        firstDay = firstDay.withMonth(m);
        prepareExpenseMap(expenseSheet, firstDay, firstDay.withDayOfMonth(firstDay.lengthOfMonth()), firstDay, firstDay.withDayOfMonth(firstDay.lengthOfMonth()));
        for (CategoryEntity category : expenseSheet.getCategoryList()) {
          CategoryExpense categoryExpense = getCategoryExpenseMap(expenseSheet, category);
          if (categoryExpense != null)
            yearCategory.getCategory((m-1), categoryExpense.getCategory()).setSum(categoryExpense.getSum());
        }
      }
      yearCategoryList.add(yearCategory);
    }
    logger.info("prepareYearCategoryList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return yearCategoryList;
  }


  public List<UserLimitEntity> getUserLimitListDesc(ExpenseSheetEntity expenseSheet) {
    List<UserLimitEntity> returnList = new ArrayList<UserLimitEntity>(expenseSheet.getUserLimitList());
    Collections.reverse(returnList);
    return returnList;
  }

  public List<UserLimitEntity> getUserLimitListRealUser(ExpenseSheetEntity expenseSheet) {
    List<UserLimitEntity> userLimitList = expenseSheet.getUserLimitList().parallelStream()
        .filter(uL -> uL.getUser() instanceof RealUserEntity)
        .collect(Collectors.toList());
    return userLimitList;
  }

  public List<UserLimitEntity> getUserLimitListNotRealUser(ExpenseSheetEntity expenseSheet) {
    List<UserLimitEntity> userLimitList = new ArrayList<UserLimitEntity>();
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList())
      if (!(userLimit.getUser() instanceof RealUserEntity))
        userLimitList.add(userLimit);
    return userLimitList;
  }

  @Transactional
  public void fetchCategoryList(ExpenseSheetEntity expenseSheet) {
    try {
      expenseSheet.getCategoryList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      ExpenseSheetEntity attached = eshr.getOne(expenseSheet.getId());
      attached.getCategoryList().size();
      expenseSheet.setCategoryList(attached.getCategoryList());
      logger.info("fetchCategoryList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  @Transactional
  public void fetchExpenseList(ExpenseSheetEntity expenseSheet) {
    try {
      expenseSheet.getExpenseList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      ExpenseSheetEntity attached = eshr.getOne(expenseSheet.getId());
      attached.getExpenseList().size();
      expenseSheet.setExpenseList(attached.getExpenseList());
      logger.info("fetchExpenseList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  @Transactional
  public void fetchUserLimitList(ExpenseSheetEntity expenseSheet) {
    try {
      expenseSheet.getUserLimitList().size();
    } catch (LazyInitializationException e) {
      LocalDateTime stopper = LocalDateTime.now();
      ExpenseSheetEntity attached = eshr.getOne(expenseSheet.getId());
      attached.getUserLimitList().size();
      expenseSheet.setUserLimitList(attached.getUserLimitList());
      logger.info("fetchUserLimitList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    }
  }

  public void addCategory(CategoryEntity category, ExpenseSheetEntity expenseSheet) {
	expenseSheet.getCategoryList().add(category);
    eshr.save(expenseSheet);
  }

  public void removeCategory(CategoryEntity category, ExpenseSheetEntity expenseSheet) {
	expenseSheet.getCategoryList().remove(category);
    int i = 0;
    for (CategoryEntity cat : expenseSheet.getCategoryList()) {
      cat.setOrder(i++);
    }
    eshr.save(expenseSheet);
  }

  public void checkSummary(ExpenseSheetEntity expenseSheet, LocalDate date) {
    LocalDateTime stopper = LocalDateTime.now();
    if (expenseSheet.getFilter() != null)
      return;
    for (UserLimitEntity userLimit : expenseSheet.getUserLimitList()) {
      uls.fetchUserSummaryList(userLimit);
      logger.debug("checkSummary for: {} at {}", userLimit, date);
      UserSummaryEntity userSummary = uss.findUserSummary(userLimit, date);
      BigDecimal exSummary = new BigDecimal(0);
      if (expenseSheet.getUserLimitExpenseMap().get(userLimit) != null)
        exSummary = expenseSheet.getUserLimitExpenseMap().get(userLimit).getSum();
      logger.debug("exSummary: {}; userSummary: {}", exSummary, userSummary.getSum());
      if (userSummary.getSum().compareTo(exSummary) != 0) {
        ShowNotification.changeSummary(userLimit.getUser().getName(), userSummary.getSum(), exSummary);
        userSummary.setSum(exSummary);
        uss.merge(userSummary);
      }
    }
    logger.info("checkSummary for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
