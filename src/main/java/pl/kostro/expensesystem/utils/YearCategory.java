package pl.kostro.expensesystem.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.kostro.expensesystem.model.Category;

public class YearCategory {
  
  int year;
  List<List<CategoryExpense>> categoryExpenseList;
  
  public YearCategory(int year, List<Category> categoryList) {
    this.year = year;
    categoryExpenseList = new ArrayList<List<CategoryExpense>>();
    for (int m=1; m<=12; m++) {
      List<CategoryExpense> monthCategoryList = new ArrayList<CategoryExpense>();
      for (Category category : categoryList) {
        CategoryExpense categoryExpense = new CategoryExpense(category);
        monthCategoryList.add(categoryExpense);
      }
      categoryExpenseList.add(monthCategoryList);
    }
  }
  
  public int getYear() {
    return year;
  }
  
  public CategoryExpense getCategory(int m, Category category) {
    for (CategoryExpense categoryEx : categoryExpenseList.get(m))
      if (categoryEx.getCategory().equals(category))
        return categoryEx;
    return null;
  }

  public List<CategoryExpense> getCategory(Category category) {
    List<CategoryExpense> categoryList = new ArrayList<CategoryExpense>();
    for (List<CategoryExpense> monthCategoryList : categoryExpenseList)
      for (CategoryExpense categoryExpense : monthCategoryList)
        if (categoryExpense.getCategory().equals(category))
          categoryList.add(categoryExpense);
    return categoryList;
  }

  public BigDecimal getMonthValue(int m) {
    BigDecimal sum = new BigDecimal(0);
    for (CategoryExpense categoryEx : categoryExpenseList.get(m))
      sum = sum.add(categoryEx.getSum());
    return sum;
  }

  public List<BigDecimal> getMonthsSum() {
    List<BigDecimal> sumList = new ArrayList<>();
    BigDecimal sum = new BigDecimal(0);
    for (int m = 0; m <= 11; m++) {
      BigDecimal value = getMonthValue(m);
      if (value != null)
        sum.add(value);
      sumList.add(sum);
    }
    return sumList;
  }

  public List<BigDecimal> getMonths() {
    List<BigDecimal> sumList = new ArrayList<>();
    for (int m = 0; m <= 11; m++) {
      BigDecimal value = getMonthValue(m);
      if (value != null)
        sumList.add(value);
    }
    return sumList;
  }
}
