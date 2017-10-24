package pl.kostro.expensesystem.model.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.kostro.expensesystem.model.mongodb.NewUser;

public interface NewUserRepository extends MongoRepository<NewUser, String> {

}
