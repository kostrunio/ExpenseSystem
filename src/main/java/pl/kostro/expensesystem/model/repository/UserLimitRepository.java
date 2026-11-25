package pl.kostro.expensesystem.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public interface UserLimitRepository extends JpaRepository<UserLimitEntity, Long> {

}
