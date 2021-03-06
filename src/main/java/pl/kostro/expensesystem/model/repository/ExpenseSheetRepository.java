package pl.kostro.expensesystem.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;

public interface ExpenseSheetRepository extends JpaRepository<ExpenseSheetEntity, Long> {

	@Query("select esh from RealUserEntity ru join ru.expenseSheetList esh where ru = :realUser")
	List<ExpenseSheetEntity> findByRealUser(@Param("realUser") RealUserEntity realUser);
}
