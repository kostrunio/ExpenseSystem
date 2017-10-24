package pl.kostro.expensesystem.model.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.kostro.expensesystem.model.mongodb.NewExpense;

public interface NewExpenseRepository extends MongoRepository<NewExpense, String> {
	
}
