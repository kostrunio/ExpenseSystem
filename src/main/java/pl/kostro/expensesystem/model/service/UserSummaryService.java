package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.entity.UserLimitEntity;
import pl.kostro.expensesystem.model.entity.UserSummaryEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserSummaryService {
    UserSummaryEntity createUserSummary(UserLimitEntity userLimit, LocalDate date);
    UserSummaryEntity merge(UserSummaryEntity userSummary);
    void decrypt(UserSummaryEntity userSummary);
    void encrypt(UserSummaryEntity userSummary);
    BigDecimal calculateSum(UserLimitEntity userLimit, LocalDate date);
    UserSummaryEntity findUserSummary(UserLimitEntity userLimit, LocalDate date);
}
