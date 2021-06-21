package pl.kostro.expensesystem.utils.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.UserEntity;

public class Filter {
  private LocalDate dateFrom;
  private LocalDate dateTo;
	private List<CategoryEntity> categories;
	private List<UserEntity> users;
	private String formula;
	private String comment;
	
	public Filter(List<CategoryEntity> categories, List<UserEntity> users, String formula, String comment) {
		super();
		this.categories = categories;
		this.users = users;
		this.formula = formula;
		this.comment = comment;
	}
	
	public Filter(LocalDate dateFrom, LocalDate dateTo, List<CategoryEntity> categories, List<UserEntity> users, String formula, String comment) {
    super();
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.categories = categories;
    this.users = users;
    this.formula = formula;
    this.comment = comment;
  }

	public LocalDate getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(LocalDate dateFrom) {
    this.dateFrom = dateFrom;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }

  public void setDateTo(LocalDate dateTo) {
    this.dateTo = dateTo;
  }

  public List<CategoryEntity> getCategories() {
		return categories;
	}
	public void setCategory(List<CategoryEntity> categories) {
		this.categories = categories;
	}

	public List<UserEntity> getUsers() {
		return users;
	}
	public void setUser(List<UserEntity> users) {
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
	
  public static boolean matchFilter(ExpenseEntity expense, Filter filter) {
    if (filter == null)
      return true;
    else {
      if (filter.getDateFrom() != null
          && expense.getDate().isBefore(filter.getDateFrom()))
        return false;
      if (filter.getDateTo() != null
          && expense.getDate().isAfter(filter.getDateTo()))
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
  
  private static boolean matchCategory(ExpenseEntity expense, List<CategoryEntity> categories) {
    if (categories.size() == 0 || categories.get(0) == null)
      return true;
    for (CategoryEntity category : categories)
      if (expense.getCategory().equals(category))
        return true;
    return false;
  }
  
  private static boolean matchUser(ExpenseEntity expense, List<UserEntity> users) {
    if (users.size() == 0 || users.get(0) == null)
      return true;
    for (UserEntity user : users)
      if (expense.getUser().equals(user))
        return true;
    return false;
  }
}
