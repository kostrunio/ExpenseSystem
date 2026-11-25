package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public interface ExpenseSheetService {
    ExpenseSheetEntity create(String name, String key, RealUserEntity owner, UserLimitEntity userLimit);
    void merge(ExpenseSheetEntity expenseSheet);
    void removeExpenseSheet(ExpenseSheetEntity expenseSheet);
    void decrypt(ExpenseSheetEntity expenseSheet);
    void encrypt(ExpenseSheetEntity expenseSheet);
    ExpenseSheetEntity moveCategoryUp(ExpenseSheetEntity expenseSheet, CategoryEntity category);
    ExpenseSheetEntity moveCategoryDown(ExpenseSheetEntity expenseSheet, CategoryEntity category);
    void fetchCategoryList(ExpenseSheetEntity expenseSheet);
    void fetchExpenseList(ExpenseSheetEntity expenseSheet);
    void fetchUserLimitList(ExpenseSheetEntity expenseSheet);
    void addCategory(CategoryEntity category, ExpenseSheetEntity expenseSheet);
    void removeCategory(CategoryEntity category, ExpenseSheetEntity expenseSheet);
}
