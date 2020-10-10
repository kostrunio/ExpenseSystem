package pl.kostro.expensesystem.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.db.model.UserLimitEntity;

public interface UserLimitRepository extends JpaRepository<UserLimitEntity, Long> {

}
