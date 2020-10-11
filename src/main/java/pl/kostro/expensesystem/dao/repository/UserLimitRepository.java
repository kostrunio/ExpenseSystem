package pl.kostro.expensesystem.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.dao.model.UserLimitEntity;

public interface UserLimitRepository extends JpaRepository<UserLimitEntity, Long> {

}
