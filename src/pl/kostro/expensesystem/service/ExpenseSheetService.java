package pl.kostro.expensesystem.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pl.kostro.expensesystem.dao.ExpenseEntityDao;
import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.User;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.utils.CategoryExpense;
import pl.kostro.expensesystem.utils.DateExpense;
import pl.kostro.expensesystem.utils.Filter;
import pl.kostro.expensesystem.utils.UserLimitExpense;

public class ExpenseSheetService {
  
  private static ExpenseService expenseService = new ExpenseService();
  
  public void removeExpenseSheet(int id) {
    ExpenseSheet eSh = findExpenseSheet(id);
    if (eSh != null) {
      ExpenseEntityDao.getEntityManager().remove(eSh);
    }
  }

  public ExpenseSheet findExpenseSheet(int id) {
    return ExpenseEntityDao.getEntityManager().find(ExpenseSheet.class, id);
  }
  
  public ExpenseSheet createExpenseSheet(RealUser owner, String name) {
    ExpenseEntityDao.begin();
    try {
      ExpenseSheet expenseSheet = new ExpenseSheet();
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      UserLimit userLimit = new UserLimit(owner, new BigDecimal(0));
      ExpenseEntityDao.getEntityManager().persist(userLimit);
      expenseSheet.setOwner(owner);
      expenseSheet.setName(name);
      expenseSheet.getUserLimitList().add(userLimit);
      expenseSheet = ExpenseEntityDao.getEntityManager().merge(expenseSheet);
      owner.getExpenseSheetList().add(expenseSheet);
      owner = ExpenseEntityDao.getEntityManager().merge(owner);
      ExpenseEntityDao.commit();
      return expenseSheet;
    } finally {
      ExpenseEntityDao.close();
    }
  }

  private static List<Expense> getExpenseList(ExpenseSheet expenseSheet) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    expenseSheet.setExpenseList(expenseService.findExpenseForDates(expenseSheet));
    for (Expense expense : expenseSheet.getExpenseList())
      if (Filter.matchFilter(expense, expenseSheet.getFilter()))
        expenseListToReturn.add(expense);
    return expenseListToReturn;
  }

  public static Map<Date, DateExpense> prepareExpenseMap(ExpenseSheet expenseSheet, Date startDate, Date endDate, Date firstDay, Date lastDay) {
    expenseSheet.getDateExpenseMap().clear();
    expenseSheet.getCategoryExpenseMap().clear();
    expenseSheet.getUserLimitExpenseMap().clear();
    expenseSheet.setFirstDate(startDate);
    expenseSheet.setLastDate(endDate);
    for (Expense expense : getExpenseList(expenseSheet)) {
      addExpenseToDateMap(expenseSheet, expense);
      if (firstDay != null && lastDay != null && !expense.getDate().before(firstDay) && !expense.getDate().after(lastDay)) {
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
    UserLimitExpense userLimitExpense = expenseSheet.getUserLimitExpenseMap().get(getUserLimitForUser(expenseSheet, expense.getUser()));
    if (userLimitExpense == null) {
      userLimitExpense = new UserLimitExpense(getUserLimitForUser(expenseSheet, expense.getUser()));
      expenseSheet.getUserLimitExpenseMap().put(getUserLimitForUser(expenseSheet, expense.getUser()), userLimitExpense);
    }
    userLimitExpense.addExpense(expense);
  }

  public void addExpense(ExpenseSheet expenseSheet, Expense expense) {
    expenseSheet.getExpenseList().add(expense);
    addExpenseToDateMap(expenseSheet, expense);
  }

  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    expenseSheet.getExpenseList().remove(expense);
    removeExpenseFromMap(expenseSheet, expense);
  }
  
  private void removeExpenseFromMap(ExpenseSheet expenseSheet, Expense expense) {
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
    for (Expense expense : ExpenseService.findAllExpense(expenseSheet))
      if (expense.getComment() != null
        && !expense.getComment().equals(""))
      commentList.add(expense.getComment());
    return commentList;
  }
  
  public static Set<String> getCommentForCategory(ExpenseSheet expenseSheet, Category category) {
    Set<String> commentList = new TreeSet<String>();
    for (Expense expense : expenseService.findExpenseByCategory(expenseSheet, category))
      if (expense.getComment() != null
        && !expense.getComment().equals(""))
      commentList.add(expense.getComment());
    return commentList;
  }
  
  public List<String> getYearList(ExpenseSheet expenseSheet) {
    List<String> yearList = new ArrayList<String>();
    int thisYear = new GregorianCalendar().get(Calendar.YEAR);
    int firstYear = thisYear;
    Calendar date = new GregorianCalendar();
    date.setTime(expenseService.findFirstExpense(expenseSheet).getDate());
    int year = date.get(Calendar.YEAR);
    if (year < firstYear)
      firstYear = year;
    for (int i = firstYear; i <= thisYear; i++)
      yearList.add(Integer.toString(i));
    return yearList;
  }
  
  public static DateExpense getDateExpenseMap(ExpenseSheet expenseSheet, Date date) {
    if (date.before(expenseSheet.getFirstDate()) || date.after(expenseSheet.getLastDate())) {
      prepareExpenseMap(expenseSheet, UserSummaryService.getFirstDay(date), UserSummaryService.getLastDay(date), null, null);
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

}
