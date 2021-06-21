package pl.kostro.expensesystem.utils.expense;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.kostro.expensesystem.model.entity.CategoryEntity;

public class YearCategory {
  
  int year;
  List<List<CategoryExpense>> categoryExpenseList;
  
  public YearCategory(int year, List<CategoryEntity> categoryList) {
    this.year = year;
    categoryExpenseList = new ArrayList<List<CategoryExpense>>();
    for (int m=0; m<=11; m++) {
      List<CategoryExpense> monthCategoryList = new ArrayList<CategoryExpense>();
      for (CategoryEntity category : categoryList) {
        CategoryExpense categoryExpense = new CategoryExpense(category);
        monthCategoryList.add(categoryExpense);
      }
      categoryExpenseList.add(monthCategoryList);
    }
  }
  
  public int getYear() {
    return year;
  }
  
  public CategoryExpense getCategory(int m, CategoryEntity category) {
    for (CategoryExpense categoryEx : categoryExpenseList.get(m))
      if (categoryEx.getCategory().equals(category))
        return categoryEx;
    return null;
  }

  public List<CategoryExpense> getCategory(CategoryEntity category) {
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

  public List<String> getMonthsSum() {
    List<BigDecimal> sumList = new ArrayList<>();
    BigDecimal sum = new BigDecimal(0);
    for (int m = 0; m <= 11; m++) {
      BigDecimal value = getMonthValue(m);
      if (value != null)
        sum.add(value);
      sumList.add(sum);
    }
    List<String> resultList = new ArrayList<>();
    for (BigDecimal value : sumList)
      resultList.add(value.toString());
    return resultList;
  }

  public List<String> getMonths() {
    List<BigDecimal> sumList = new ArrayList<>();
    for (int m = 0; m <= 11; m++) {
      BigDecimal value = getMonthValue(m);
      if (value != null)
        sumList.add(value);
    }
    List<String> resultList = new ArrayList<>();
    for (BigDecimal value : sumList)
      resultList.add(value.toString());
    return resultList;
  }
}
