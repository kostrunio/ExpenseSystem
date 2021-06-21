package pl.kostro.expensesystem.model.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kostro.expensesystem.model.entity.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
	
	@Query("select e from Expense e where e.notify = true and e.date = :date")
	List<ExpenseEntity> findExpensesToNotify(@Param("date") LocalDate date);
}
