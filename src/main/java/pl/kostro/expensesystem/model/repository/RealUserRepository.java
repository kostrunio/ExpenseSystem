package pl.kostro.expensesystem.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUserEntity;

public interface RealUserRepository extends JpaRepository<RealUserEntity, Long> {
	
	RealUserEntity findByName(String name);
	
	RealUserEntity findByNameAndPassword(String name, String password);

	@Query("select u from RealUser u join u.expenseSheetList es where es = :expenseSheet order by es.id")
	List<RealUserEntity> findUsersWithExpenseSheet(@Param("expenseSheet") ExpenseSheet expenseSheet);
}
