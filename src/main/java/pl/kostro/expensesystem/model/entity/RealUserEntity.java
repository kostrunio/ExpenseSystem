package pl.kostro.expensesystem.model.entity;

import lombok.Getter;
import lombok.Setter;
import pl.kostro.expensesystem.model.converter.LocalDateTimePersistenceConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value="2")
public class RealUserEntity extends UserEntity {
  @Setter
  @Getter
  @Column(name = "u_password")
  private String password;
  @Column(name = "u_password_byte")
  private byte[] password_byte;
  @Setter
  @Getter
  @Column(name = "u_email")
  private String email;
  @Setter
  @Getter
  @Column(name = "u_log_date")
  @Convert(converter = LocalDateTimePersistenceConverter.class)
  private LocalDateTime logDate;
  @Setter
  @ManyToMany
  @JoinTable(name="user_expense_sheet",
  joinColumns=
      @JoinColumn(name="ues_u_id", referencedColumnName="u_id"),
  inverseJoinColumns=
      @JoinColumn(name="ues_es_id", referencedColumnName="es_id")
  )
  @OrderBy
  private List<ExpenseSheetEntity> expenseSheetList;
  @Setter
  @Getter
  @OneToOne
  @JoinColumn(name = "u_default_es_id")
  private ExpenseSheetEntity defaultExpenseSheet;
  @Setter
  @Getter
  @Transient
  String clearPassword;

  public RealUserEntity() {
    super();
  }

  public RealUserEntity(String name) {
    super(name);
  }

  public byte[] getPasswordByte() {
    return password_byte;
  }

  public void setPasswordByte(byte[] password_byte) {
    this.password_byte = password_byte;
  }

  public List<ExpenseSheetEntity> getExpenseSheetList() {
    if (expenseSheetList == null)
      expenseSheetList = new ArrayList<>();
    return expenseSheetList;
  }

}
