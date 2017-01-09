package pl.kostro.expensesystem.model.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.vaadin.server.VaadinSession;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.calendar.CalendarUtils;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;
import pl.kostro.expensesystem.utils.expense.YearCategory;

public class ExpenseSheetService {

  private static Logger logger = LogManager.getLogger();

  public static ExpenseSheet createExpenseSheet(RealUser owner, String name, String key) {
    ExpenseEntityDao.begin();
    ExpenseSheet expenseSheet = new ExpenseSheet();
    expenseSheet.setEncrypted(true);
    expenseSheet.setKey(key);
    VaadinSession.getCurrent().setAttribute(ExpenseSheet.class, expenseSheet);
    try {
      UserLimit userLimit = new UserLimit(owner, 0);
      ExpenseEntityDao.getEntityManager().persist(userLimit);
      expenseSheet.setOwner(owner);
      expenseSheet.setName(name);
      expenseSheet.getUserLimitList().add(userLimit);
      ExpenseEntityDao.getEntityManager().persist(expenseSheet);
      owner.getExpenseSheetList().add(expenseSheet);
      ExpenseEntityDao.getEntityManager().merge(owner);
      ExpenseEntityDao.commit();
    } finally {
    }
    if (owner.getDefaultExpenseSheet() == null)
      RealUserService.setDefaultExpenseSheet(owner, expenseSheet);
    return expenseSheet;
  }

  public static void merge(ExpenseSheet expenseSheet) {
    ExpenseEntityDao.begin();
    try {
      ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
    }
  }

  public static void removeExpenseSheet(ExpenseSheet expenseSheet) {
    ExpenseEntityDao.begin();
    try {
      List<RealUser> realUsers = null;
      realUsers = ExpenseEntityDao.findByNamedQueryWithParameters("findUsersWithExpenseSheet",
          ImmutableMap.of("expenseSheet", expenseSheet), RealUser.class);
      if (realUsers != null)
        for (RealUser realUser : realUsers) {
          if (realUser.getDefaultExpenseSheet() != null && realUser.getDefaultExpenseSheet().equals(expenseSheet))
            realUser.setDefaultExpenseSheet(null);
          realUser.getExpenseSheetList().remove(expenseSheet);
        }
      ExpenseEntityDao.getEntityManager().remove(expenseSheet);
      ExpenseEntityDao.commit();
    } finally {
    }
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

  public static void encrypt(ExpenseSheet expenseSheet) {
    ExpenseEntityDao.begin();
    try {
      logger.info("encrypt: category");
      for (Category category : expenseSheet.getCategoryList())
        CategoryService.encrypt(category);
      logger.info("encrypt: expense");
      for (Expense expense : expenseSheet.getExpenseList())
        ExpenseService.encrypt(expense);
      logger.info("encrypt: userLimit");
      for (UserLimit userLimit : expenseSheet.getUserLimitList())
        UserLimitService.encrypt(userLimit);
      ExpenseEntityDao.commit();
    } catch (Exception e) {
      e.printStackTrace();
      ExpenseEntityDao.rollback();
    }
  }

  public static ExpenseSheet findExpenseSheet(RealUser realUser, int id) {
    for (ExpenseSheet expenseSheet : realUser.getExpenseSheetList())
      if (expenseSheet.getId() == id)
        return expenseSheet;
    return realUser.getDefaultExpenseSheet();
  }

  private static List<Expense> getExpenseList(ExpenseSheet expenseSheet) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : ExpenseService.findExpenseForDates(expenseSheet))
      if (Filter.matchFilter(expense, expenseSheet.getFilter()))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }

  public static Map<Date, DateExpense> prepareExpenseMap(ExpenseSheet expenseSheet, Date startDate, Date endDate,
      Date firstDay, Date lastDay) {
    expenseSheet.getDateExpenseMap().clear();
    expenseSheet.getCategoryExpenseMap().clear();
    expenseSheet.getUserLimitExpenseMap().clear();
    expenseSheet.setFirstDate(startDate);
    expenseSheet.setLastDate(endDate);
    for (Expense expense : getExpenseList(expenseSheet)) {
      addExpenseToDateMap(expenseSheet, expense);
      if (firstDay != null && lastDay != null && !expense.getDate().before(firstDay)
          && !expense.getDate().after(lastDay)) {
        addExpenseToCategoryMap(expenseSheet, expense);
        addExpenseToUserLimitMap(expenseSheet, expense);
      }
    }
    UserSummaryService.checkSummary(expenseSheet, firstDay);
    return expenseSheet.getDateExpenseMap();
  }

  private static void addExpenseToDateMap(ExpenseSheet expenseSheet, Expense expense) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    if (dateExpense == null) {
      dateExpense = new DateExpense(expense.getDate());
      expenseSheet.getDateExpenseMap().put(expense.getDate(), dateExpense);
    }
    dateExpense.addExpense(expense);
  }

  private static void addExpenseToCategoryMap(ExpenseSheet expenseSheet, Expense expense) {
    CategoryExpense categoryExpense = expenseSheet.getCategoryExpenseMap().get(expense.getCategory());
    if (categoryExpense == null) {
      categoryExpense = new CategoryExpense(expense.getCategory());
      expenseSheet.getCategoryExpenseMap().put(expense.getCategory(), categoryExpense);
    }
    categoryExpense.addExpense(expense);
  }

  private static void addExpenseToUserLimitMap(ExpenseSheet expenseSheet, Expense expense) {
    UserLimit userLimit = getUserLimitForUser(expenseSheet, expense.getUser());
    UserLimitExpense userLimitExpense = expenseSheet.getUserLimitExpenseMap().get(userLimit);
    if (userLimitExpense == null) {
      userLimitExpense = new UserLimitExpense(userLimit);
      expenseSheet.getUserLimitExpenseMap().put(userLimitExpense.getUserLimit(), userLimitExpense);
    }
    userLimitExpense.addExpense(expense);
  }

  public static void addExpense(ExpenseSheet expenseSheet, Expense expense) {
    expenseSheet.getExpenseList().add(expense);
    addExpenseToDateMap(expenseSheet, expense);
  }

  public static void removeExpenseFromMap(ExpenseSheet expenseSheet, Expense expense) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    dateExpense.removeExpense(expense);
  }

  public static UserLimit getUserLimitForUser(ExpenseSheet expenseSheet, User user) {
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      if (userLimit.getUser().equals(user))
        return userLimit;
    return null;
  }

  public static Set<String> getAllComments(ExpenseSheet expenseSheet) {
    Set<String> commentList = new TreeSet<String>();
    for (Expense expense : expenseSheet.getExpenseList())
      if (expense.getComment() != null && !expense.getComment().equals(""))
        commentList.add(expense.getComment());
    return commentList;
  }

  public static Set<String> getCommentForCategory(ExpenseSheet expenseSheet, Category category) {
    Set<String> commentList = new TreeSet<String>();
    for (Expense expense : ExpenseService.findExpenseByCategory(expenseSheet, category))
      if (expense.getComment() != null && !expense.getComment().equals(""))
        commentList.add(expense.getComment());
    return commentList;
  }

  public static List<String> getYearList(ExpenseSheet expenseSheet) {
    List<String> yearList = new ArrayList<String>();
    int thisYear = new GregorianCalendar().get(Calendar.YEAR);
    int firstYear = thisYear;
    Calendar date = new GregorianCalendar();
    date.setTime(ExpenseService.findFirstExpense(expenseSheet).getDate());
    int year = date.get(Calendar.YEAR);
    if (year < firstYear)
      firstYear = year;
    for (int i = firstYear; i <= thisYear; i++)
      yearList.add(Integer.toString(i));
    return yearList;
  }

  public static DateExpense getDateExpenseMap(ExpenseSheet expenseSheet, Date date) {
    if (date.before(expenseSheet.getFirstDate()) || date.after(expenseSheet.getLastDate())) {
      prepareExpenseMap(expenseSheet, CalendarUtils.getFirstDay(date), CalendarUtils.getLastDay(date), null,
          null);
    }
    return expenseSheet.getDateExpenseMap().get(date);
  }

  public static CategoryExpense getCategoryExpenseMap(ExpenseSheet expenseSheet, Category category) {
    return expenseSheet.getCategoryExpenseMap().get(category);
  }

  public static UserLimitExpense getUserLimitExpenseMap(ExpenseSheet expenseSheet, UserLimit userLimit) {
    return expenseSheet.getUserLimitExpenseMap().get(userLimit);
  }

  public static Set<String> getCommentsList(ExpenseSheet expenseSheet) {
    Set<String> commentsList = new TreeSet<String>();
    for (Expense expense : expenseSheet.getExpenseList()) {
      if (expense.getComment() != null && !expense.getComment().equals(""))
        commentsList.add(expense.getComment());
    }
    return commentsList;
  }

  public static ExpenseSheet moveCategoryUp(ExpenseSheet expenseSheet, Category category) {
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
    return expenseSheet;
  }

  public static ExpenseSheet moveCategoryDown(ExpenseSheet expenseSheet, Category category) {
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
    return expenseSheet;
  }

  public static List<YearCategory> prepareYearCategoryList(ExpenseSheet expenseSheet) {
    List<YearCategory> yearCategoryList = new ArrayList<YearCategory>();
    Calendar firstDay = Calendar.getInstance();
    for (String year : ExpenseSheetService.getYearList(expenseSheet)) {
      YearCategory yearCategory = new YearCategory(Integer.parseInt(year), expenseSheet.getCategoryList());
      CalendarUtils.setFirstDay(firstDay, year);
      for (int m = 0; m <= 11; m++) {
        firstDay.set(Calendar.MONTH, m);
        ExpenseSheetService.prepareExpenseMap(expenseSheet, firstDay.getTime(),
            CalendarUtils.getLastDay(firstDay.getTime()), firstDay.getTime(),
            CalendarUtils.getLastDay(firstDay.getTime()));
        for (Category category : expenseSheet.getCategoryList()) {
          CategoryExpense categoryExpense = ExpenseSheetService.getCategoryExpenseMap(expenseSheet, category);
          if (categoryExpense != null)
            yearCategory.getCategory(m, categoryExpense.getCategory()).setSum(categoryExpense.getSum());
        }
      }
      yearCategoryList.add(yearCategory);
    }
    return yearCategoryList;
  }


  public static List<UserLimit> getUserLimitListDesc(ExpenseSheet expenseSheet) {
    List<UserLimit> returnList = new ArrayList<UserLimit>(expenseSheet.getUserLimitList());
    Collections.reverse(returnList);
    return returnList;
  }

  public static List<UserLimit> getUserLimitListRealUser(ExpenseSheet expenseSheet) {
    List<UserLimit> userLimitList = new ArrayList<UserLimit>();
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      if (userLimit.getUser() instanceof RealUser)
        userLimitList.add(userLimit);
    return userLimitList;
  }

  public static List<UserLimit> getUserLimitListNotRealUser(ExpenseSheet expenseSheet) {
    List<UserLimit> userLimitList = new ArrayList<UserLimit>();
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      if (!(userLimit.getUser() instanceof RealUser))
        userLimitList.add(userLimit);
    return userLimitList;
  }
}
