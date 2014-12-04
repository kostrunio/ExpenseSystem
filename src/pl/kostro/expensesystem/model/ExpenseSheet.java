package pl.kostro.expensesystem.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.DateExpense;

@Entity
public class ExpenseSheet {

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  @OneToOne
  private RealUser owner;
  private String name;
  @OneToMany(fetch=FetchType.EAGER)
  @OrderBy("orderId")
  private List<Category> categoryList;
  @OneToMany(fetch=FetchType.EAGER)
  @OrderBy
  private List<UserLimit> userLimitList;
  @OneToMany(fetch=FetchType.EAGER)
  @OrderBy
  private List<Expense> expenseList;
  private int reloadeDay;
  private int mainLimit;
  @Transient
  private Map<Date, DateExpense> dateExpenseMap;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public RealUser getOwner() {
    return owner;
  }

  public void setOwner(RealUser owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Category> getCategoryList() {
    if (categoryList == null)
      categoryList = new ArrayList<Category>();
    return categoryList;
  }

  public void setCategoryList(List<Category> categoryList) {
    this.categoryList = categoryList;
  }

  public List<UserLimit> getUserLimitList() {
    if (userLimitList == null)
      userLimitList = new ArrayList<UserLimit>();
    return userLimitList;
  }

  public void setUserLimitList(List<UserLimit> userLimitList) {
    this.userLimitList = userLimitList;
  }

  public List<Expense> getExpenseList() {
    if (expenseList == null)
      expenseList = new ArrayList<Expense>();
    return expenseList;
  }

  public void setExpenseList(List<Expense> expenseList) {
    this.expenseList = expenseList;
  }

  public int getReloadeDay() {
    return reloadeDay;
  }

  public void setReloadeDay(int reloadeDay) {
    this.reloadeDay = reloadeDay;
  }

  public int getMainLimit() {
    return mainLimit;
  }

  public void setMainLimit(int mainLimit) {
    this.mainLimit = mainLimit;
  }

  public Map<Date, DateExpense> getDateExpenseMap() {
    if (dateExpenseMap == null)
      dateExpenseMap = new HashMap<Date, DateExpense>();
    return dateExpenseMap;
  }

  public void setDateExpenseMap(Map<Date, DateExpense> dateExpenseMap) {
    this.dateExpenseMap = dateExpenseMap;
  }

  private List<Expense> getExpenseList(Date startDate, Date endDate) {
    List<Expense> expenseListToReturn = new ArrayList<Expense>();
    for (Expense expense : getExpenseList()) {
      if (expense.getDate().equals(startDate)
          || expense.getDate().equals(endDate)
          || (expense.getDate().after(startDate) && expense.getDate().before(endDate)))
        expenseListToReturn.add(expense);
    }
    return expenseListToReturn;
  }

  public Map<Date, DateExpense> prepareDateExpenseMap(Date startDate, Date endDate) {
    getDateExpenseMap().clear();
    for (Expense expense : getExpenseList(startDate, endDate)) {
      addExpenseToMap(expense);
    }
    return getDateExpenseMap();
  }

  private void addExpenseToMap(Expense expense) {
    DateExpense dateExpense = getDateExpenseMap().get(expense.getDate());
    if (dateExpense == null) {
      dateExpense = new DateExpense(expense.getDate());
      getDateExpenseMap().put(expense.getDate(), dateExpense);
    }
    dateExpense.addExpense(expense);
  }

  public void addExpense(Expense expense) {
    getExpenseList().add(expense);
    addExpenseToMap(expense);
  }

  public void removeExpense(Expense expense) {
    getExpenseList().remove(expense);
    removeExpenseFromMap(expense);
  }
  
  private void removeExpenseFromMap(Expense expense) {
    DateExpense dateExpense = getDateExpenseMap().get(expense.getDate());
    dateExpense.removeExpense(expense);
  }
  
  public UserLimit getUserLimitForUser(User user) {
    for (UserLimit userLimit : getUserLimitList())
      if (userLimit.getUser() == user)
        return userLimit;
    return null;
  }
  
  public Set<String> getCommentForCategory(Category category) {
    Set<String> commentList = new TreeSet<String>();
    for (Expense expense : getExpenseList())
      if (expense.getCategory() == category)
        if (expense.getComment() != null
        && !expense.getComment().equals(new String()))
        commentList.add(expense.getComment());
    return commentList;
  }

  public static void createExpenseSheet(RealUser loggedUser) {
    ExpenseSheet expenseSheet = new ExpenseSheet();
    expenseSheet.setId(1);
    expenseSheet.setOwner(loggedUser);
    expenseSheet.setName("wydatki");
    Category.createCategory(expenseSheet.getCategoryList());
    UserLimit.createUserLimit(expenseSheet);
    Expense.createExpense(expenseSheet);
    loggedUser.getExpenseSheetList().add(expenseSheet);

    expenseSheet = new ExpenseSheet();
    expenseSheet.setId(2);
    expenseSheet.setOwner(loggedUser);
    expenseSheet.setName("mieszkanie");
    Category.createCategory(expenseSheet.getCategoryList());
    UserLimit.createUserLimit(expenseSheet);
    Expense.createExpense(expenseSheet);
    loggedUser.getExpenseSheetList().add(expenseSheet);
  }

}
