package pl.kostro.expensesystem.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.dao.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
