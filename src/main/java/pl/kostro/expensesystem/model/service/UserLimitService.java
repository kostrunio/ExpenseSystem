package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.entity.UserEntity;
import pl.kostro.expensesystem.model.entity.UserLimitEntity;

public interface UserLimitService {
    UserLimitEntity create(UserEntity user, int orderId);
    void merge(UserLimitEntity userLimit);
    void remove(UserLimitEntity userLimit);

    void fetchUserSummaryList(UserLimitEntity userLimit);
}
