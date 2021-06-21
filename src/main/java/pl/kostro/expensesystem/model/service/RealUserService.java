package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUserEntity;

public interface RealUserService {
    RealUserEntity createRealUser(String name, String password, String email);
    void merge(RealUserEntity realUser, boolean passwordChange);
    RealUserEntity refresh(RealUserEntity realUser);
    void setDefaultExpenseSheet(RealUserEntity realUser, ExpenseSheet expenseSheet);
    RealUserEntity getUserData(String userName, String password);
    RealUserEntity findRealUser(String userName);
    void fetchExpenseSheetList(RealUserEntity realUser);
}
