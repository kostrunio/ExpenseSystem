package pl.kostro.expensesystem.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.model.UserSummary;

public interface UserSummaryRepository extends JpaRepository<UserSummary, Long> {

}
