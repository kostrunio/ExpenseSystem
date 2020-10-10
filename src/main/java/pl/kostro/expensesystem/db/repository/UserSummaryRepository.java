package pl.kostro.expensesystem.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.db.model.UserSummaryEntity;

public interface UserSummaryRepository extends JpaRepository<UserSummaryEntity, Long> {

}
