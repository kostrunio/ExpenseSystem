package pl.kostro.expensesystem.model.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.kostro.expensesystem.model.mongodb.NewCategory;

public interface NewCategoryRepository extends MongoRepository<NewCategory, String> {

}
