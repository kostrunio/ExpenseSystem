package pl.kostro.expensesystem.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.model.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
