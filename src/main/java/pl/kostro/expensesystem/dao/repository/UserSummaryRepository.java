package pl.kostro.expensesystem.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.dao.model.UserSummaryEntity;

public interface UserSummaryRepository extends JpaRepository<UserSummaryEntity, Long> {

}
