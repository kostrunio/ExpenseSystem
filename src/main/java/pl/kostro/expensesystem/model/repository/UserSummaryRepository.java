package pl.kostro.expensesystem.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.model.UserSummaryEntity;

public interface UserSummaryRepository extends JpaRepository<UserSummaryEntity, Long> {

}
