package pl.kostro.expensesystem.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.dao.model.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
