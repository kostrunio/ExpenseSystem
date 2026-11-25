package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.entity.CategoryEntity;

public interface CategoryService {
    void save(CategoryEntity category);
    void remove(CategoryEntity category);
}
