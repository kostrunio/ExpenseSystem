package pl.kostro.expensesystem.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
