package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.UserEntity;
import pl.kostro.expensesystem.model.UserLimitEntity;

public interface UserLimitService {
    UserLimitEntity create(UserEntity user, int orderId);
    void merge(UserLimitEntity userLimit);
    void remove(UserLimitEntity userLimit);
    void decrypt(UserLimitEntity userLimit);
    void encrypt(UserLimitEntity userLimit);
    void fetchUserSummaryList(UserLimitEntity userLimit);
}
