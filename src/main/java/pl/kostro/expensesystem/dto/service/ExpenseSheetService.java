package pl.kostro.expensesystem.dto.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.dao.service.ExpenseSheetDao;
import pl.kostro.expensesystem.dao.service.RealUserDao;
import pl.kostro.expensesystem.dao.service.UserLimitDao;
import pl.kostro.expensesystem.dto.model.ExpenseSheet;
import pl.kostro.expensesystem.dto.model.Category;
import pl.kostro.expensesystem.dto.model.Expense;
import pl.kostro.expensesystem.dto.model.RealUser;
import pl.kostro.expensesystem.dto.model.User;
import pl.kostro.expensesystem.dto.model.UserLimit;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;
import pl.kostro.expensesystem.utils.expense.YearCategory;

@Service
public class ExpenseSheetService {
  
  @Autowired
  private ExpenseSheetDao eshs;
  @Autowired
  private UserLimitDao uld;
  @Autowired
  private RealUserDao rud;
  
  @Autowired
  private CategoryService cs;
  @Autowired
  private ExpenseService es;
  @Autowired
  private RealUserService rus;
  @Autowired
  private UserLimitService uls;

  private static Logger logger = LogManager.getLogger();

  public ExpenseSheet createExpenseSheet(RealUser owner, String name, String key) {
    LocalDateTime stopper = LocalDateTime.now();
    ExpenseSheet expenseSheet = new ExpenseSheet();
    expenseSheet.setEncrypted(true);
    expenseSheet.setKey(key);
    expenseSheet.setOwner(owner);
    expenseSheet.setName(name);
    VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, expenseSheet);
    UserLimit userLimit = new UserLimit(owner, 0);
    expenseSheet.getUserLimitList().add(userLimit);
    uld.save(userLimit);
    owner.getExpenseSheetList().add(expenseSheet);
    eshs.save(expenseSheet);
    if (owner.getDefaultExpenseSheet() == null)
      rus.setDefaultExpenseSheet(owner, expenseSheet);
    logger.info("createExpenseSheet for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public void merge(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    eshs.merge(expenseSheet);
    logger.info("merge for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeExpenseSheet(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<RealUser> realUsers = rud.findUsersWithExpenseSheet(expenseSheet);
    if (realUsers != null)
      for (RealUser realUser : realUsers) {
        if (realUser.getDefaultExpenseSheet() != null && realUser.getDefaultExpenseSheet().equals(expenseSheet))
          realUser.setDefaultExpenseSheet(null);
        realUser.getExpenseSheetList().remove(expenseSheet);
      }
    eshs.delete(expenseSheet);
    logger.info("removeExpenseSheet for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public ExpenseSheet findExpenseSheet(RealUser realUser, int id) {
    Optional<ExpenseSheet> result = realUser.getExpenseSheetList().stream()
      .filter(esh -> esh.getId() == id)
      .findFirst();
    if (result.isPresent())
      return result.get();
    return realUser.getDefaultExpenseSheet();
  }

  public List<Expense> findAllExpense(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseListToReturn = expenseSheet.getExpenseList().stream()
        .filter(e -> Filter.matchFilter(e, expenseSheet.getFilter()))
        .collect(Collectors.toList());
    ExpenseService.logger.info("findAllExpense for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseListToReturn;
  }

  private List<Expense> getExpenseList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseListToReturn = expenseSheet.getExpenseList().stream()
    .filter(e -> !e.getDate().isBefore(expenseSheet.getFirstDate()))
    .filter(e -> !e.getDate().isAfter(expenseSheet.getLastDate()))
    .filter(e -> Filter.matchFilter(e, expenseSheet.getFilter()))
    .collect(Collectors.toList());
    logger.info("getExpenseList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseListToReturn;
  }

  public Map<LocalDate, DateExpense> prepareExpenseMap(ExpenseSheet expenseSheet, LocalDate startDate, LocalDate endDate,
                                                       LocalDate firstDay, LocalDate lastDay) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.getDateExpenseMap().clear();
    expenseSheet.getCategoryExpenseMap().clear();
    expenseSheet.getUserLimitExpenseMap().clear();
    expenseSheet.setFirstDate(startDate);
    expenseSheet.setLastDate(endDate);
    for (Expense expense : getExpenseList(expenseSheet)) {
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

  private void addExpenseToDateMap(ExpenseSheet expenseSheet, Expense expense) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    if (dateExpense == null) {
      dateExpense = new DateExpense(expense.getDate());
      expenseSheet.getDateExpenseMap().put(expense.getDate(), dateExpense);
    }
    dateExpense.addExpense(expenseSheet, expense);
  }

  private void addExpenseToCategoryMap(ExpenseSheet expenseSheet, Expense expense) {
    CategoryExpense categoryExpense = expenseSheet.getCategoryExpenseMap().get(expense.getCategory());
    if (categoryExpense == null) {
      categoryExpense = new CategoryExpense(expense.getCategory());
      expenseSheet.getCategoryExpenseMap().put(expense.getCategory(), categoryExpense);
    }
    categoryExpense.addExpense(expense);
  }

  private void addExpenseToUserLimitMap(ExpenseSheet expenseSheet, Expense expense) {
    UserLimit userLimit = getUserLimitForUser(expenseSheet, expense.getUser());
    UserLimitExpense userLimitExpense = expenseSheet.getUserLimitExpenseMap().get(userLimit);
    if (userLimitExpense == null) {
      userLimitExpense = new UserLimitExpense(userLimit);
      expenseSheet.getUserLimitExpenseMap().put(userLimitExpense.getUserLimit(), userLimitExpense);
    }
    userLimitExpense.addExpense(expense);
  }

  public void addExpense(ExpenseSheet expenseSheet, Expense expense) {
    expenseSheet.getExpenseList().add(expense);
    addExpenseToDateMap(expenseSheet, expense);
  }

  public void removeExpenseFromMap(ExpenseSheet expenseSheet, Expense expense) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    if (dateExpense != null)
      dateExpense.removeExpense(expense);
  }

  public UserLimit getUserLimitForUser(ExpenseSheet expenseSheet, User user) {
    Optional<UserLimit> result = expenseSheet.getUserLimitList().stream()
      .filter(ul -> ul.getUser().equals(user))
      .findFirst();
    if (result.isPresent())
      return result.get();
    else
      return null;
  }

  public Set<String> getAllComments(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    Set<String> commentsList = expenseSheet.getExpenseList().stream()
        .filter(e -> e.getComment() != null && !e.getComment().isEmpty())
        .map(e -> e.getComment())
        .sorted()
        .collect(Collectors.toSet());
    logger.info("getAllComments for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return commentsList;
  }

  public Set<String> getCommentForCategory(ExpenseSheet expenseSheet, Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    Set<String> commentsList = expenseSheet.getExpenseList().stream()
        .filter(e -> e.getCategory().equals(category))
        .filter(e -> e.getComment() != null && !e.getComment().isEmpty())
        .map(e -> e.getComment())
        .sorted()
        .collect(Collectors.toSet());
    logger.info("getCommentForCategory for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return commentsList;
  }

  public List<String> getYearList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<String> yearList = new ArrayList<String>();
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

  public DateExpense getDateExpenseMap(ExpenseSheet expenseSheet, LocalDate date) {
    if (date.isBefore(expenseSheet.getFirstDate()) || date.isAfter(expenseSheet.getLastDate())) {
      prepareExpenseMap(expenseSheet, date.withDayOfMonth(1), date.withDayOfMonth(date.lengthOfMonth()), null, null);
    }
    return expenseSheet.getDateExpenseMap().get(date);
  }

  public CategoryExpense getCategoryExpenseMap(ExpenseSheet expenseSheet, Category category) {
    return expenseSheet.getCategoryExpenseMap().get(category);
  }

  public UserLimitExpense getUserLimitExpenseMap(ExpenseSheet expenseSheet, UserLimit userLimit) {
    return expenseSheet.getUserLimitExpenseMap().get(userLimit);
  }

  public ExpenseSheet moveCategoryUp(ExpenseSheet expenseSheet, Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    if (category.getOrder() == 0)
      return expenseSheet;
    category.setOrder(category.getOrder() - 1);
    for (Category cat : expenseSheet.getCategoryList()) {
      if (cat.getOrder() == category.getOrder())
        if (!cat.equals(category))
          cat.setOrder(cat.getOrder() + 1);
    }
    Category tmp = expenseSheet.getCategoryList().get(category.getOrder());
    expenseSheet.getCategoryList().set(category.getOrder() + 1, tmp);
    expenseSheet.getCategoryList().set(category.getOrder(), category);

    merge(expenseSheet);
    logger.info("moveCategoryUp for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public ExpenseSheet moveCategoryDown(ExpenseSheet expenseSheet, Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    if (category.getOrder() == expenseSheet.getCategoryList().size())
      return expenseSheet;
    category.setOrder(category.getOrder() + 1);
    for (Category cat : expenseSheet.getCategoryList()) {
      if (cat.getOrder() == category.getOrder())
        if (!cat.equals(category))
          cat.setOrder(cat.getOrder() - 1);
    }
    Category tmp = expenseSheet.getCategoryList().get(category.getOrder());
    expenseSheet.getCategoryList().set(category.getOrder() - 1, tmp);
    expenseSheet.getCategoryList().set(category.getOrder(), category);

    merge(expenseSheet);
    logger.info("moveCategoryDown for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public List<YearCategory> prepareYearCategoryList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<YearCategory> yearCategoryList = new ArrayList<YearCategory>();
    LocalDate firstDay = LocalDate.now();
    for (String year : getYearList(expenseSheet)) {
      YearCategory yearCategory = new YearCategory(Integer.parseInt(year), expenseSheet.getCategoryList());
      firstDay = firstDay.withYear(Integer.parseInt(year)).withDayOfMonth(1);
      for (int m = 1; m <= 12; m++) {
        firstDay = firstDay.withMonth(m);
        prepareExpenseMap(expenseSheet, firstDay, firstDay.withDayOfMonth(firstDay.lengthOfMonth()), firstDay, firstDay.withDayOfMonth(firstDay.lengthOfMonth()));
        for (Category category : expenseSheet.getCategoryList()) {
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


  public List<UserLimit> getUserLimitListDesc(ExpenseSheet expenseSheet) {
    List<UserLimit> returnList = new ArrayList<UserLimit>(expenseSheet.getUserLimitList());
    Collections.reverse(returnList);
    return returnList;
  }

  public List<UserLimit> getUserLimitListRealUser(ExpenseSheet expenseSheet) {
    List<UserLimit> userLimitList = expenseSheet.getUserLimitList().stream()
        .filter(uL -> uL.getUser() instanceof RealUser)
        .collect(Collectors.toList());
    return userLimitList;
  }

  public List<UserLimit> getUserLimitListNotRealUser(ExpenseSheet expenseSheet) {
    List<UserLimit> userLimitList = new ArrayList<>();
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      if (!(userLimit.getUser() instanceof RealUser))
        userLimitList.add(userLimit);
    return userLimitList;
  }

  @Transactional
  public void fetchCategoryList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    eshs.fetchCategoryList(expenseSheet);
    logger.info("fetchCategoryList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  @Transactional
  public void fetchExpenseList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    eshs.fetchExpenseList(expenseSheet);
    logger.info("fetchExpenseList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  @Transactional
  public void fetchUserLimitList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    eshs.fetchUserLimitList(expenseSheet);
    logger.info("fetchUserLimitList for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public static void decrypt(ExpenseSheet expenseSheet) {
    logger.info("decrypt: category");
    for (Category category : expenseSheet.getCategoryList())
      CategoryService.decrypt(category);
    logger.info("decrypt: expense");
    for (Expense expense : expenseSheet.getExpenseList())
      ExpenseService.decrypt(expense);
    logger.info("decrypt: userLimit");
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      UserLimitService.decrypt(userLimit);
  }

  public void encrypt(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    logger.info("encrypt: category");
    for (Category category : expenseSheet.getCategoryList())
      cs.encrypt(category);
    logger.info("encrypt: expense");
    for (Expense expense : expenseSheet.getExpenseList())
      es.encrypt(expense);
    logger.info("encrypt: userLimit");
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      uls.encrypt(userLimit);
    logger.info("encrypt for {} finish: {} ms", expenseSheet, stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
