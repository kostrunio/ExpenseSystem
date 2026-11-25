package pl.kostro.expensesystem.model.service;

import org.junit.jupiter.api.Test;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.model.repository.RealUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RealUserServiceImplTest {

    @Test
    void removingSheetAccessAlsoClearsTheUsersDefaultSheet() {
        RealUserRepository repository = mock(RealUserRepository.class);
        RealUserEntity user = new RealUserEntity("shared-user");
        user.setId(42L);
        ExpenseSheetEntity sheet = new ExpenseSheetEntity();
        user.setExpenseSheetList(new ArrayList<>(List.of(sheet)));
        user.setDefaultExpenseSheet(sheet);
        when(repository.findById(42L)).thenReturn(Optional.of(user));

        new RealUserServiceImpl(repository).removeExpenseSheetAccess(user, sheet);

        assertThat(user.getExpenseSheetList()).doesNotContain(sheet);
        assertThat(user.getDefaultExpenseSheet()).isNull();
        verify(repository).save(user);
    }
}
