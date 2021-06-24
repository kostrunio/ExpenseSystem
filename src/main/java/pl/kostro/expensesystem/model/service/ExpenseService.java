package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.entity.ExpenseEntity;

import java.util.List;

public interface ExpenseService {
    void save(ExpenseEntity expense);
    void remove(ExpenseEntity expense);

    List<ExpenseEntity> findExpensesToNotify();
}
