package pl.kostro.expensesystem.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kostro.expensesystem.dao.model.ExpenseSheetEntity;
import pl.kostro.expensesystem.dao.model.RealUserEntity;

public interface ExpenseSheetRepository extends JpaRepository<ExpenseSheetEntity, Long> {

	@Query("select esh from RealUser ru join ru.expenseSheetList esh where ru = :realUser")
	List<ExpenseSheetEntity> findByRealUser(@Param("realUser") RealUserEntity realUser);
}
