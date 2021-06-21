package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.*;

import java.util.List;

public interface ExpenseService {
    void save(ExpenseEntity expense);
    void remove(ExpenseEntity expense);
    void decrypt(ExpenseEntity expense);
    void encrypt(ExpenseEntity expense);

    List<ExpenseEntity> findExpensesToNotify();
}
