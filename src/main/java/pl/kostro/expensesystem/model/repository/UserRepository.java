package pl.kostro.expensesystem.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
