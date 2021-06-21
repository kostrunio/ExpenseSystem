package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.UserEntity;

public interface UserService {
    UserEntity createAndSave(String name);
}
