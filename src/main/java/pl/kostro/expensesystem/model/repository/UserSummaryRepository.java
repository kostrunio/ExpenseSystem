package pl.kostro.expensesystem.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.model.entity.UserSummaryEntity;

public interface UserSummaryRepository extends JpaRepository<UserSummaryEntity, Long> {

}
