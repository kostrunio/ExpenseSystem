package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.CategoryEntity;
import pl.kostro.expensesystem.model.ExpenseSheet;

public interface CategoryService {
    void save(CategoryEntity category);
    void remove(CategoryEntity category);
    void decrypt(CategoryEntity category);
    void encrypt(CategoryEntity category);
}
