package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.*;
import pl.kostro.expensesystem.utils.expense.CategoryExpense;
import pl.kostro.expensesystem.utils.expense.DateExpense;
import pl.kostro.expensesystem.utils.expense.UserLimitExpense;
import pl.kostro.expensesystem.utils.expense.YearCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExpenseSheetService {
    ExpenseSheetEntity create(String name, String key, RealUserEntity owner, UserLimitEntity userLimit);
    void merge(ExpenseSheetEntity expenseSheet);
    void removeExpenseSheet(ExpenseSheetEntity expenseSheet);
    void decrypt(ExpenseSheetEntity expenseSheet);
    void encrypt(ExpenseSheetEntity expenseSheet);
    ExpenseSheetEntity findExpenseSheet(RealUserEntity realUser, int id);
    List<ExpenseEntity> findAllExpense(ExpenseSheetEntity expenseSheet);
    Map<LocalDate, DateExpense> prepareExpenseMap(ExpenseSheetEntity expenseSheet, LocalDate startDate, LocalDate endDate, LocalDate firstDay, LocalDate lastDay);
    void addExpense(ExpenseEntity expense, ExpenseSheetEntity expenseSheet);
    void removeExpense(ExpenseEntity expense, ExpenseSheetEntity expenseSheet);
    UserLimitEntity getUserLimitForUser(ExpenseSheetEntity expenseSheet, UserEntity user);
    Set<String> getAllComments(ExpenseSheetEntity expenseSheet);
    Set<String> getCommentForCategory(ExpenseSheetEntity expenseSheet, CategoryEntity category);
    List<String> getYearList(ExpenseSheetEntity expenseSheet);
    DateExpense getDateExpenseMap(ExpenseSheetEntity expenseSheet, LocalDate date);
    CategoryExpense getCategoryExpenseMap(ExpenseSheetEntity expenseSheet, CategoryEntity category);
    UserLimitExpense getUserLimitExpenseMap(ExpenseSheetEntity expenseSheet, UserLimitEntity userLimit);
    ExpenseSheetEntity moveCategoryUp(ExpenseSheetEntity expenseSheet, CategoryEntity category);
    ExpenseSheetEntity moveCategoryDown(ExpenseSheetEntity expenseSheet, CategoryEntity category);
    List<YearCategory> prepareYearCategoryList(ExpenseSheetEntity expenseSheet);
    List<UserLimitEntity> getUserLimitListDesc(ExpenseSheetEntity expenseSheet);
    List<UserLimitEntity> getUserLimitListRealUser(ExpenseSheetEntity expenseSheet);
    List<UserLimitEntity> getUserLimitListNotRealUser(ExpenseSheetEntity expenseSheet);
    void fetchCategoryList(ExpenseSheetEntity expenseSheet);
    void fetchExpenseList(ExpenseSheetEntity expenseSheet);
    void fetchUserLimitList(ExpenseSheetEntity expenseSheet);
    void addCategory(CategoryEntity category, ExpenseSheetEntity expenseSheet);
    void removeCategory(CategoryEntity category, ExpenseSheetEntity expenseSheet);
    void checkSummary(ExpenseSheetEntity expenseSheet, LocalDate date);

}
