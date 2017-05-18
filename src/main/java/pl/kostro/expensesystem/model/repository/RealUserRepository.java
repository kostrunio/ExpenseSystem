package pl.kostro.expensesystem.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

public interface RealUserRepository extends JpaRepository<RealUser, Long> {
	
	RealUser findByName(String name);
	
	RealUser findByNameAndPassword(String name, String password);

	@Query("select sh from RealUser ru join ru.expenseSheetList sh where ru = :realUser")
	List<ExpenseSheet> fetchExpenseSheetList(@Param("realUser") RealUser realUser);
	
	@Query("select u from RealUser u join u.expenseSheetList es where es = :expenseSheet")
	List<RealUser> findUsersWithExpenseSheet(@Param("expenseSheet") ExpenseSheet expenseSheet);
}
