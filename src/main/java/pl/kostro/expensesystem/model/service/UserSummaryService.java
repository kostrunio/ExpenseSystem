package pl.kostro.expensesystem.model.service;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummaryEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserSummaryService {
    UserSummaryEntity createUserSummary(UserLimit userLimit, LocalDate date);
    UserSummaryEntity merge(UserSummaryEntity userSummary);
    void decrypt(UserSummaryEntity userSummary);
    void encrypt(UserSummaryEntity userSummary);
    BigDecimal calculateSum(UserLimit userLimit, LocalDate date);
    UserSummaryEntity findUserSummary(UserLimit userLimit, LocalDate date);
}
