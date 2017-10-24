package pl.kostro.expensesystem.model.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.kostro.expensesystem.model.mongodb.NewUserLimit;

public interface NewUserLimitRepository extends MongoRepository<NewUserLimit, String> {

}
