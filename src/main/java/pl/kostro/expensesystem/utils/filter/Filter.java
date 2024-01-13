package pl.kostro.expensesystem.utils.filter;

import lombok.Getter;
import lombok.Setter;
import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class Filter {
  @Setter
  private LocalDate dateFrom;
  @Setter
  private LocalDate dateTo;
	private List<CategoryEntity> categories;
	private List<UserEntity> users;
	@Setter
    private String formula;
	@Setter
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

    public void setCategory(List<CategoryEntity> categories) {
		this.categories = categories;
	}

    public void setUser(List<UserEntity> users) {
		this.users = users;
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
          && !expense.getFormula().contains(filter.getFormula().replace('.', ','))
              && !expense.getFormula().contains(filter.getFormula().replace(',', '.'))
          && !expense.getValue().equals(new BigDecimal(filter.getFormula().replace(',', '.'))))
        return false;
      return filter.getComment() == null || filter.getComment().isEmpty()
          || ((expense.getComment() == null
                || expense.getComment().contains(filter.getComment()))
            && expense.getComment() != null);
    }
  }
  
  private static boolean matchCategory(ExpenseEntity expense, List<CategoryEntity> categories) {
    if (categories.isEmpty() || categories.get(0) == null)
      return true;
    for (CategoryEntity category : categories)
      if (expense.getCategory().equals(category))
        return true;
    return false;
  }
  
  private static boolean matchUser(ExpenseEntity expense, List<UserEntity> users) {
    if (users.isEmpty() || users.get(0) == null)
      return true;
    for (UserEntity user : users)
      if (expense.getUser().equals(user))
        return true;
    return false;
  }
}
