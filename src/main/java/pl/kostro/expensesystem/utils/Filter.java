package pl.kostro.expensesystem.utils;

import java.math.BigDecimal;
import java.util.Date;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.User;

public class Filter {
  private Date dateFrom;
  private Date dateTo;
	private Category category;
	private User user;
	private String formula;
	private String comment;
	
	public Filter(Category category, User user, String formula, String comment) {
		super();
		this.category = category;
		this.user = user;
		this.formula = formula;
		this.comment = comment;
	}
	
	public Filter(Date dateFrom, Date dateTo, Category category, User user, String formula, String comment) {
    super();
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.category = category;
    this.user = user;
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

  public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
          && expense.getDate().before(filter.getDateFrom()))
        return false;
      if (filter.getDateTo() != null
          && expense.getDate().after(filter.getDateTo()))
        return false;
      if (filter.getCategory() != null
          && !expense.getCategory().equals(filter.getCategory()))
        return false;
      if (filter.getUser() != null
          && !expense.getUser().equals(filter.getUser()))
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
}
