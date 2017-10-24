package pl.kostro.expensesystem.model.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.kostro.expensesystem.model.mongodb.NewUserSummary;

public interface NewUserSummaryRepository extends MongoRepository<NewUserSummary, String> {

}
