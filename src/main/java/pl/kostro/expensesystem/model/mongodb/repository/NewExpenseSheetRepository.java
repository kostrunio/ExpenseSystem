package pl.kostro.expensesystem.model.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.kostro.expensesystem.model.mongodb.NewExpenseSheet;

public interface NewExpenseSheetRepository extends MongoRepository<NewExpenseSheet, String> {

}
