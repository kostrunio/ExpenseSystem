package pl.kostro.expensesystem.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.db.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
