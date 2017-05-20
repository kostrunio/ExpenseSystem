package pl.kostro.expensesystem.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kostro.expensesystem.model.Category;
import pl.kostro.expensesystem.model.Expense;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.UserLimit;

public interface ExpenseSheetRepository extends JpaRepository<ExpenseSheet, Long> {

	@Query("select esh from RealUser ru join ru.expenseSheetList esh where ru = :realUser")
	List<ExpenseSheet> findByRealUser(@Param("realUser") RealUser realUser);
	
	 @Query("select c from ExpenseSheet esh join esh.categoryList c where esh = :expenseSheet order by c.order")
	  List<Category> findCategoryList(@Param("expenseSheet") ExpenseSheet expenseSheet);
	 
	  @Query("select e from ExpenseSheet esh join esh.expenseList e where esh = :expenseSheet order by e.id")
	  List<Expense> findExpenseList(@Param("expenseSheet") ExpenseSheet expenseSheet);

	  @Query("select ul from ExpenseSheet esh join esh.userLimitList ul where esh = :expenseSheet order by ul.order")
    List<UserLimit> findUserLimitList(@Param("expenseSheet") ExpenseSheet expenseSheet);
}
