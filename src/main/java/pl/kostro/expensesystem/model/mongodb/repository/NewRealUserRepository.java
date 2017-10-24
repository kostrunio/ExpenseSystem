package pl.kostro.expensesystem.model.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.kostro.expensesystem.model.mongodb.NewRealUser;

public interface NewRealUserRepository extends MongoRepository<NewRealUser, String> {
	
	NewRealUser findByName(String name);
	
	NewRealUser findByNameAndPassword(String name, String password);

}
