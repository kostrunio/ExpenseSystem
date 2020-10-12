package pl.kostro.expensesystem.utils;

import org.springframework.beans.BeanUtils;
import pl.kostro.expensesystem.dao.model.*;
import pl.kostro.expensesystem.dto.model.*;

import java.math.BigDecimal;

public class Converter {
    public static Category toCategory(CategoryEntity categoryEntity) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryEntity, category);
        category.setName(Encryption.decryption(categoryEntity.getNameByte()));
        return category;
    }

    public static CategoryEntity toCategoryEntity(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        toCategoryEntity(category, categoryEntity);
        return categoryEntity;
    }

    public static void toCategoryEntity(Category category, CategoryEntity categoryEntity) {
        BeanUtils.copyProperties(category, categoryEntity);
        categoryEntity.setNameByte(Encryption.encryption(category.getName()));
    }

    public static Expense toExpense(ExpenseEntity expenseEntity) {
        Expense expense = new Expense();
        BeanUtils.copyProperties(expenseEntity, expense, "category", "user", "expenseSheet");
        expense.setCategory(toCategory(expenseEntity.getCategory()));
        expense.setUser(toUser(expenseEntity.getUser()));
        expense.setExpenseSheet(toExpenseSheet(expenseEntity.getExpenseSheet(), false));

        expense.setFormula(Encryption.decryption(expenseEntity.getFormulaByte()));
        expense.setComment(Encryption.decryption(expenseEntity.getCommentByte()));
        return expense;
    }

    public static ExpenseEntity toExpenseEntity(Expense expense) {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        toExpenseEntity(expense, expenseEntity);
        return expenseEntity;
    }

    public static void toExpenseEntity(Expense expense, ExpenseEntity expenseEntity) {
        BeanUtils.copyProperties(expense, expenseEntity, "category", "user", "expenseSheet");
        expenseEntity.setCategory(toCategoryEntity(expense.getCategory()));
        expenseEntity.setUser(toUserEntity(expense.getUser()));
        expenseEntity.setExpenseSheet(toExpenseSheetEntity(expense.getExpenseSheet()));

        expenseEntity.setFormulaByte(Encryption.encryption(expense.getFormula()));
        expenseEntity.setCommentByte(Encryption.encryption(expense.getComment()));
    }

    public static ExpenseSheet toExpenseSheet(ExpenseSheetEntity expenseSheetEntity, boolean all) {
        ExpenseSheet expenseSheet = new ExpenseSheet();
        toExpenseSheet(expenseSheetEntity, expenseSheet, all);
        return expenseSheet;
    }

    public static void toExpenseSheet(ExpenseSheetEntity expenseSheetEntity, ExpenseSheet expenseSheet, boolean all) {
        BeanUtils.copyProperties(expenseSheetEntity, expenseSheet, "owner", "categoryList", "userLimitList", "expenseList");
        if (all) {
            expenseSheet.setOwner(toRealUser(expenseSheetEntity.getOwner(), false));
            for (CategoryEntity categoryEntity : expenseSheetEntity.getCategoryList())
                expenseSheet.getCategoryList().add(toCategory(categoryEntity));
            for (UserLimitEntity userLimitEntity : expenseSheetEntity.getUserLimitList())
                expenseSheet.getUserLimitList().add(toUserLimit(userLimitEntity));
            for (ExpenseEntity expenseEntity : expenseSheetEntity.getExpenseList())
                expenseSheet.getExpenseList().add(toExpense(expenseEntity));
        }
    }

    public static ExpenseSheetEntity toExpenseSheetEntity(ExpenseSheet expenseSheet) {
        ExpenseSheetEntity expenseSheetEntity = new ExpenseSheetEntity();
        toExpenseSheetEntity(expenseSheet, expenseSheetEntity);
        return expenseSheetEntity;
    }

    public static void toExpenseSheetEntity(ExpenseSheet expenseSheet, ExpenseSheetEntity expenseSheetEntity) {
        BeanUtils.copyProperties(expenseSheet, expenseSheetEntity, "owner", "categoryList", "userLimitList", "expenseList");
        expenseSheetEntity.setOwner(toRealUserEntity(expenseSheet.getOwner()));
        for (Category category : expenseSheet.getCategoryList())
            expenseSheetEntity.getCategoryList().add(toCategoryEntity(category));
        for (UserLimit userLimit : expenseSheet.getUserLimitList())
            expenseSheetEntity.getUserLimitList().add(toUserLimitEntity(userLimit));
        for (Expense expense : expenseSheet.getExpenseList())
            expenseSheetEntity.getExpenseList().add(toExpenseEntity(expense));
    }

    public static RealUser toRealUser(RealUserEntity realUserEntity, boolean all) {
        RealUser realUser = new RealUser();
        toRealUser(realUserEntity, realUser, all);
        return realUser;
    }

    public static void toRealUser(RealUserEntity realUserEntity, RealUser realUser, boolean all) {
        BeanUtils.copyProperties(realUserEntity, realUser, "expenseSheetList", "defaultExpenseSheet");
        if (all) {
            for (ExpenseSheetEntity expenseSheetEntity : realUserEntity.getExpenseSheetList())
                realUser.getExpenseSheetList().add(toExpenseSheet(expenseSheetEntity, false));
            realUser.setDefaultExpenseSheet(toExpenseSheet(realUserEntity.getDefaultExpenseSheet(), false));
        }
    }

    public static RealUserEntity toRealUserEntity(RealUser realUser) {
        RealUserEntity realUserEntity = new RealUserEntity();
        toRealUserEntity(realUser, realUserEntity);
        return realUserEntity;
    }

    public static RealUserEntity toRealUserEntity(RealUser realUser, RealUserEntity realUserEntity) {
        BeanUtils.copyProperties(realUser, realUserEntity, "expenseSheetList", "defaultExpenseSheet");
        for (ExpenseSheet expenseSheet : realUser.getExpenseSheetList())
            realUserEntity.getExpenseSheetList().add(toExpenseSheetEntity(expenseSheet));
        realUserEntity.setDefaultExpenseSheet(toExpenseSheetEntity(realUser.getDefaultExpenseSheet()));
        return realUserEntity;
    }

    public static User toUser(UserEntity userEntity) {
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        return user;
    }

    public static UserEntity toUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        return userEntity;
    }

    public static UserLimit toUserLimit(UserLimitEntity userLimitEntity) {
        UserLimit userLimit = new UserLimit();
        BeanUtils.copyProperties(userLimitEntity, userLimit, "user");
        userLimit.setUser(toUser(userLimitEntity.getUser()));

        userLimit.setLimit(new BigDecimal(Encryption.decryption(userLimitEntity.getLimitByte())));
        return userLimit;
    }

    public static UserLimitEntity toUserLimitEntity(UserLimit userLimit) {
        UserLimitEntity userLimitEntity = new UserLimitEntity();
        toUserLimitEntity(userLimit, userLimitEntity);
        return userLimitEntity;
    }

    public static void toUserLimitEntity(UserLimit userLimit, UserLimitEntity userLimitEntity) {
        BeanUtils.copyProperties(userLimit, userLimitEntity, "user");
        userLimitEntity.setUser(toUserEntity(userLimit.getUser()));

        userLimitEntity.setLimitByte(Encryption.encryption(userLimit.getLimit().toString()));
    }

    public static UserSummary toUserSummary(UserSummaryEntity userSummaryEntity) {
        UserSummary userSummary = new UserSummary();
        BeanUtils.copyProperties(userSummaryEntity, userSummary);
        userSummary.setLimit(new BigDecimal(Encryption.decryption(userSummaryEntity.getLimitByte())));
        userSummary.setSum(new BigDecimal(Encryption.decryption(userSummaryEntity.getSumByte())));
        return userSummary;
    }

    public static UserSummaryEntity toUserSummaryEntity(UserSummary userSummary) {
        UserSummaryEntity userSummaryEntity = new UserSummaryEntity();
        toUserSummaryEntity(userSummary, userSummaryEntity);
        return userSummaryEntity;
    }

    public static void toUserSummaryEntity(UserSummary userSummary, UserSummaryEntity userSummaryEntity) {
        BeanUtils.copyProperties(userSummary, userSummaryEntity);
        userSummaryEntity.setLimitByte(Encryption.encryption(userSummary.getLimit().toString()));
        userSummaryEntity.setSumByte(Encryption.encryption(userSummary.getSum().toString()));
    }
}
