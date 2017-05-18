package pl.kostro.expensesystem.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kostro.expensesystem.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
