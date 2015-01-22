package pl.kostro.expensesystem.service;

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
import pl.kostro.expensesystem.utils.DateExpense;

public class ExpenseSheetService {
  
  private ExpenseService expenseService = new ExpenseService();
  
  public void removeProfessor(int id) {
    ExpenseSheet emp = findExpenseSheet(id);
    if (emp != null) {
      ExpenseEntityDao.getEntityManager().remove(emp);
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
      UserLimit userLimit = new UserLimit(owner, 0, expenseSheet);
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

  private List<Expense> getExpenseList(ExpenseSheet expenseSheet, Date startDate, Date endDate) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    expenseSheet.setExpenseList(expenseService.findExpenseForDates(expenseSheet, startDate, endDate));
    for (Expense expense : expenseSheet.getExpenseList()) {
      if (startDate.equals(expense.getDate())
          || endDate.equals(expense.getDate())
          || (expense.getDate().after(startDate) && expense.getDate().before(endDate)))
        expenseListToReturn.add(expense);
    }
    return expenseListToReturn;
  }

  public Map<Date, DateExpense> prepareDateExpenseMap(ExpenseSheet expenseSheet, Date startDate, Date endDate) {
    expenseSheet.getDateExpenseMap().clear();
    for (Expense expense : getExpenseList(expenseSheet, startDate, endDate)) {
      addExpenseToMap(expenseSheet, expense);
    }
    return expenseSheet.getDateExpenseMap();
  }

  private void addExpenseToMap(ExpenseSheet expenseSheet, Expense expense) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    if (dateExpense == null) {
      dateExpense = new DateExpense(expense.getDate());
      expenseSheet.getDateExpenseMap().put(expense.getDate(), dateExpense);
    }
    dateExpense.addExpense(expense);
  }

  public void addExpense(ExpenseSheet expenseSheet, Expense expense) {
    expenseSheet.getExpenseList().add(expense);
    addExpenseToMap(expenseSheet, expense);
  }

  public void removeExpense(ExpenseSheet expenseSheet, Expense expense) {
    expenseSheet.getExpenseList().remove(expense);
    removeExpenseFromMap(expenseSheet, expense);
  }
  
  private void removeExpenseFromMap(ExpenseSheet expenseSheet, Expense expense) {
    DateExpense dateExpense = expenseSheet.getDateExpenseMap().get(expense.getDate());
    dateExpense.removeExpense(expense);
  }
  
  public UserLimit getUserLimitForUser(ExpenseSheet expenseSheet, User user) {
    for (UserLimit userLimit : expenseSheet.getUserLimitList())
      if (userLimit.getUser().equals(user))
        return userLimit;
    return null;
  }
  
  public Set<String> getCommentForCategory(ExpenseSheet expenseSheet, Category category) {
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
    for (int i = firstYear-1; i <= thisYear+1; i++)
      yearList.add(Integer.toString(i));
    return yearList;
  }

}
