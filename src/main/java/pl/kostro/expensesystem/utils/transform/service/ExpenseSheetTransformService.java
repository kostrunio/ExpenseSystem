package pl.kostro.expensesystem.utils.transform.service;

import pl.kostro.expensesystem.model.entity.CategoryEntity;
import pl.kostro.expensesystem.model.entity.ExpenseEntity;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.utils.transform.model.CategoryExpense;
import pl.kostro.expensesystem.utils.transform.model.DateExpense;
import pl.kostro.expensesystem.utils.transform.model.YearCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExpenseSheetTransformService {
    Map<RealUserEntity, Map<ExpenseSheetEntity, List<ExpenseEntity>>> prepareExpenseSheetNotify(List<ExpenseEntity> expenseList);
    ExpenseSheetEntity findExpenseSheet(RealUserEntity realUser, int id);
    List<ExpenseEntity> findAllExpenses(ExpenseSheetEntity expenseSheet);
    void prepareExpenseMap(ExpenseSheetEntity expenseSheet, LocalDate startDate, LocalDate endDate, LocalDate firstDay, LocalDate lastDay);
    void addExpense(ExpenseEntity expense, ExpenseSheetEntity expenseSheet);
    void removeExpense(ExpenseEntity expense, ExpenseSheetEntity expenseSheet);
    UserLimitEntity getUserLimitForUser(UserEntity user, ExpenseSheetEntity expenseSheet);
    Set<String> getAllComments(ExpenseSheetEntity expenseSheet);
    Set<String> getCommentForCategory(ExpenseSheetEntity expenseSheet, CategoryEntity category);
    List<String> getYearList(ExpenseSheetEntity expenseSheet);
    DateExpense getDateExpenseMap(ExpenseSheetEntity expenseSheet, LocalDate date);
    CategoryExpense getCategoryExpenseMap(ExpenseSheetEntity expenseSheet, CategoryEntity category);
    List<YearCategory> prepareYearCategoryList(ExpenseSheetEntity expenseSheet);
    List<UserLimitEntity> getUserLimitListDesc(ExpenseSheetEntity expenseSheet);
    List<UserLimitEntity> getUserLimitListRealUser(ExpenseSheetEntity expenseSheet);
    List<UserLimitEntity> getUserLimitListNotRealUser(ExpenseSheetEntity expenseSheet);
    void checkSummary(ExpenseSheetEntity expenseSheet, LocalDate date);

}
