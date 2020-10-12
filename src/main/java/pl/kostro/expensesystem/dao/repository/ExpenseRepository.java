package pl.kostro.expensesystem.dao.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kostro.expensesystem.dao.model.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
	
	@Query("select e from ExpenseEntity e where e.notify = true and e.date = :date")
	List<ExpenseEntity> findExpensesToNotify(@Param("date") LocalDate date);
}
