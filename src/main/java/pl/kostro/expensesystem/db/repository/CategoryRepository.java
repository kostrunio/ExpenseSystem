package pl.kostro.expensesystem.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.db.model.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
