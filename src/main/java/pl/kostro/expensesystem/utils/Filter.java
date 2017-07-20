package pl.kostro.expensesystem.utils;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.User;

public class Filter {
  private Date dateFrom;
  private Date dateTo;
	private List<Category> categories;
	private List<User> users;
	private String formula;
	private String comment;
	
	public Filter(List<Category> categories, List<User> users, String formula, String comment) {
		super();
		this.categories = categories;
		this.users = users;
		this.formula = formula;
		this.comment = comment;
	}
	
	public Filter(Date dateFrom, Date dateTo, List<Category> categories, List<User> users, String formula, String comment) {
    super();
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.categories = categories;
    this.users = users;
    this.formula = formula;
    this.comment = comment;
  }

	public Date getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }

  public Date getDateTo() {
    return dateTo;
  }

  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }

  public List<Category> getCategories() {
		return categories;
	}
	public void setCategory(List<Category> categories) {
		this.categories = categories;
	}

	public List<User> getUsers() {
		return users;
	}
	public void setUser(List<User> users) {
		this.users = users;
	}

	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
  public static boolean matchFilter(Expense expense, Filter filter) {
    if (filter == null)
      return true;
    else {
      if (filter.getDateFrom() != null
          && expense.getDate().isBefore(filter.getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
        return false;
      if (filter.getDateTo() != null
          && expense.getDate().isAfter(filter.getDateTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
        return false;
      if (filter.getCategories() != null
          && !matchCategory(expense, filter.getCategories()))
        return false;
      if (filter.getUsers() != null
          && !matchUser(expense, filter.getUsers()))
        return false;
      if (filter.getFormula() != null
          && !expense.getFormula().contains(filter.getFormula())
          && !expense.getValue().equals(new BigDecimal(filter.getFormula())))
        return false;
      if (filter.getComment() != null && !filter.getComment().equals("")
          && ((expense.getComment() != null
            && !expense.getComment().contains(filter.getComment()))
          || expense.getComment() == null))
        return false;
      return true;
    }
  }
  
  private static boolean matchCategory(Expense expense, List<Category> categories) {
    if (categories.size() == 0 || categories.get(0) == null)
      return true;
    for (Category category : categories)
      if (expense.getCategory().equals(category))
        return true;
    return false;
  }
  
  private static boolean matchUser(Expense expense, List<User> users) {
    if (users.size() == 0 || users.get(0) == null)
      return true;
    for (User user : users)
      if (expense.getUser().equals(user))
        return true;
    return false;
  }
}
