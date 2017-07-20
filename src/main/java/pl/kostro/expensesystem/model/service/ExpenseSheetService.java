package pl.kostro.expensesystem.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.repository.ExpenseSheetRepository;
import pl.kostro.expensesystem.model.repository.RealUserRepository;
import pl.kostro.expensesystem.model.repository.UserLimitRepository;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;
import pl.kostro.expensesystem.utils.expense.YearCategory;

@Service
public class ExpenseSheetService {
  
  @Autowired
  private ExpenseSheetRepository eshr;
  @Autowired
  private UserLimitRepository ulr;
  @Autowired
  private RealUserRepository rur;
  
  @Autowired
  private CategoryService cs;
  @Autowired
  private ExpenseService es;
  @Autowired
  private RealUserService rus;
  @Autowired
  private UserLimitService uls;
  @Autowired
  private UserSummaryService uss;

  private static Logger logger = LogManager.getLogger();

  public ExpenseSheet createExpenseSheet(RealUser owner, String name, String key) {
    LocalDateTime stopper = LocalDateTime.now();
    ExpenseSheet expenseSheet = new ExpenseSheet();
    expenseSheet.setEncrypted(true);
    expenseSheet.setKey(key);
    VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, expenseSheet);
    UserLimit userLimit = new UserLimit(owner, 0);
    ulr.save(userLimit);
    expenseSheet.setOwner(owner);
    expenseSheet.setName(name);
    expenseSheet.getUserLimitList().add(userLimit);
    eshr.save(expenseSheet);
    owner.getExpenseSheetList().add(expenseSheet);
    rur.save(owner);
    if (owner.getDefaultExpenseSheet() == null)
      rus.setDefaultExpenseSheet(owner, expenseSheet);
    logger.info("createExpenseSheet finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public void merge(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    eshr.save(expenseSheet);
    logger.info("merge finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void removeExpenseSheet(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<RealUser> realUsers = null;
    realUsers = rur.findUsersWithExpenseSheet(expenseSheet);
    if (realUsers != null)
      for (RealUser realUser : realUsers) {
        if (realUser.getDefaultExpenseSheet() != null && realUser.getDefaultExpenseSheet().equals(expenseSheet))
          realUser.setDefaultExpenseSheet(null);
        realUser.getExpenseSheetList().remove(expenseSheet);
      }
    eshr.delete(expenseSheet);
    logger.info("removeExpenseSheet finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public void decrypt(ExpenseSheet expenseSheet) {
    logger.info("decrypt: category");
    for (Category category : expenseSheet.getCategoryList())
      cs.decrypt(category);
    logger.info("decrypt: expense");
    for (Expense expense : expenseSheet.getExpenseList())
      ExpenseService.decrypt(expense);
    logger.info("decrypt: userLimit");
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      uls.decrypt(userLimit);
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
    logger.info("encrypt finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }

  public ExpenseSheet findExpenseSheet(RealUser realUser, int id) {
    for (ExpenseSheet expenseSheet : realUser.getExpenseSheetList())
      if (expenseSheet.getId() == id)
        return expenseSheet;
    return realUser.getDefaultExpenseSheet();
  }

  private List<Expense> getExpenseList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : es.findExpenseForDates(expenseSheet))
      if (Filter.matchFilter(expense, expenseSheet.getFilter()))
        expenseListToReturn.add(expense);
    logger.info("getExpenseList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
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
      addExpenseToDateMap(expenseSheet, expense);
      if (firstDay != null && lastDay != null && !expense.getDate().isBefore(firstDay)
          && !expense.getDate().isAfter(lastDay)) {
        addExpenseToCategoryMap(expenseSheet, expense);
        addExpenseToUserLimitMap(expenseSheet, expense);
      }
    }
    uss.checkSummary(expenseSheet, firstDay);
    logger.info("prepareExpenseMap finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
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
    dateExpense.removeExpense(expense);
  }

  public UserLimit getUserLimitForUser(ExpenseSheet expenseSheet, User user) {
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      if (userLimit.getUser().equals(user))
        return userLimit;
    return null;
  }

  public Set<String> getAllComments(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    Set<String> commentList = new TreeSet<String>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getComment() != null && !expense.getComment().equals(""))
        commentList.add(expense.getComment());
    logger.info("getAllComments finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return commentList;
  }

  public Set<String> getCommentForCategory(ExpenseSheet expenseSheet, Category category) {
    LocalDateTime stopper = LocalDateTime.now();
    Set<String> commentList = new TreeSet<String>();
    for (Expense expense : es.findExpenseByCategory(expenseSheet, category))
      if (expense.getComment() != null && !expense.getComment().equals(""))
        commentList.add(expense.getComment());
    logger.info("getCommentForCategory finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return commentList;
  }

  public List<String> getYearList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<String> yearList = new ArrayList<String>();
    int thisYear = new GregorianCalendar().get(Calendar.YEAR);
    int firstYear = thisYear;
    LocalDate date = LocalDate.now();
    date = es.findFirstExpense(expenseSheet).getDate();
    int year = date.getYear();
    if (year < firstYear)
      firstYear = year;
    for (int i = firstYear; i <= thisYear; i++)
      yearList.add(Integer.toString(i));
    logger.info("getYearList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return yearList;
  }

  public DateExpense getDateExpenseMap(ExpenseSheet expenseSheet, LocalDate date) {
    if (date.isBefore(expenseSheet.getFirstDate()) || date.isAfter(expenseSheet.getLastDate())) {
      prepareExpenseMap(expenseSheet,
          CalendarUtils.getFirstDay(date),
          CalendarUtils.getLastDay(date), null,
          null);
    }
    return expenseSheet.getDateExpenseMap().get(date);
  }

  public CategoryExpense getCategoryExpenseMap(ExpenseSheet expenseSheet, Category category) {
    return expenseSheet.getCategoryExpenseMap().get(category);
  }

  public UserLimitExpense getUserLimitExpenseMap(ExpenseSheet expenseSheet, UserLimit userLimit) {
    return expenseSheet.getUserLimitExpenseMap().get(userLimit);
  }

  public Set<String> getCommentsList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    Set<String> commentsList = new TreeSet<String>();
    for (Expense expense : expenseSheet.getExpenseList()) {
      if (expense.getComment() != null && !expense.getComment().equals(""))
        commentsList.add(expense.getComment());
    }
    logger.info("getCommentsList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return commentsList;
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
    logger.info("moveCategoryUp finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
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
    logger.info("moveCategoryDown finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return expenseSheet;
  }

  public List<YearCategory> prepareYearCategoryList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    List<YearCategory> yearCategoryList = new ArrayList<YearCategory>();
    LocalDate firstDay = LocalDate.now();
    for (String year : getYearList(expenseSheet)) {
      YearCategory yearCategory = new YearCategory(Integer.parseInt(year), expenseSheet.getCategoryList());
      firstDay = CalendarUtils.setFirstDay(firstDay, year);
      for (int m = 1; m <= 12; m++) {
        firstDay = firstDay.withMonth(m);
        prepareExpenseMap(expenseSheet, firstDay,
            CalendarUtils.getLastDay(firstDay), firstDay,
            CalendarUtils.getLastDay(firstDay));
        for (Category category : expenseSheet.getCategoryList()) {
          CategoryExpense categoryExpense = getCategoryExpenseMap(expenseSheet, category);
          if (categoryExpense != null)
            yearCategory.getCategory(m, categoryExpense.getCategory()).setSum(categoryExpense.getSum());
        }
      }
      yearCategoryList.add(yearCategory);
    }
    logger.info("prepareYearCategoryList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
    return yearCategoryList;
  }


  public List<UserLimit> getUserLimitListDesc(ExpenseSheet expenseSheet) {
    List<UserLimit> returnList = new ArrayList<UserLimit>(expenseSheet.getUserLimitList());
    Collections.reverse(returnList);
    return returnList;
  }

  public List<UserLimit> getUserLimitListRealUser(ExpenseSheet expenseSheet) {
    List<UserLimit> userLimitList = new ArrayList<UserLimit>();
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      if (userLimit.getUser() instanceof RealUser)
        userLimitList.add(userLimit);
    return userLimitList;
  }

  public List<UserLimit> getUserLimitListNotRealUser(ExpenseSheet expenseSheet) {
    List<UserLimit> userLimitList = new ArrayList<UserLimit>();
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      if (!(userLimit.getUser() instanceof RealUser))
        userLimitList.add(userLimit);
    return userLimitList;
  }
  
  public void fetchCategoryList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.setCategoryList(eshr.findCategoryList(expenseSheet));
    logger.info("fetchCategoryList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
  
  public void fetchExpenseList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.setExpenseList(eshr.findExpenseList(expenseSheet));
    logger.info("fetchExpenseList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
  
  public void fetchUserLimitList(ExpenseSheet expenseSheet) {
    LocalDateTime stopper = LocalDateTime.now();
    expenseSheet.setUserLimitList(eshr.findUserLimitList(expenseSheet));
    logger.info("fetchUserLimitList finish: {} ms", stopper.until(LocalDateTime.now(), ChronoUnit.MILLIS));
  }
}
