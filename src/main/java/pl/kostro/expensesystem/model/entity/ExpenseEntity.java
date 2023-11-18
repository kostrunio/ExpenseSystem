package pl.kostro.expensesystem.model.entity;

import org.hibernate.annotations.GenericGenerator;
import pl.kostro.expensesystem.model.converter.LocalDatePersistenceConverter;
import pl.kostro.expensesystem.utils.calculator.Calculator;
import pl.kostro.expensesystem.utils.encryption.Encryption;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class ExpenseEntity extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "e_id")
  private Long id;
  @Column(name = "e_date")
  @Convert(converter = LocalDatePersistenceConverter.class)
  private LocalDate date;
  @Transient
  private String formula;
  @Column(name = "e_formula_byte")
  private byte[] formula_byte;
  @Transient
  private BigDecimal value;
  @OneToOne
  @JoinColumn(name = "e_c_id")
  private CategoryEntity category;
  @OneToOne
  @JoinColumn(name = "e_u_id")
  private UserEntity user;
  @Transient
  private String comment;
  @Column(name = "e_comment_byte")
  private byte[] comment_byte;
  @Column(name = "e_notify")
  private boolean notify;
  @Column(name = "e_u_date")
  @Convert(converter = LocalDatePersistenceConverter.class)
  private LocalDate updateDate;
  @ManyToOne
  @JoinColumn(name = "e_es_id")
  private ExpenseSheetEntity expenseSheet;
  @Transient
  private boolean encoded = true;

  public ExpenseEntity() {
    super();
  }

  public ExpenseEntity(LocalDate date, String formula, CategoryEntity category, UserEntity user, String comment, boolean notify,
                       ExpenseSheetEntity expenseSheet) {
    this.date = date;
    setFormula(formula);
    this.category = category;
    this.user = user;
    setComment(comment);
    this.notify = notify;
    this.expenseSheet = expenseSheet;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getFormula() {
    if (encoded) decode();
    return formula;
  }

  private void decode() {
    if (formula_byte != null)
      formula = Encryption.decryption(formula_byte);
    if (comment_byte != null)
      comment = Encryption.decryption(comment_byte);
    encoded = false;
  }

  public void setFormula(String formula) {
    this.formula_byte = Encryption.encryption(formula);
    this.formula = formula;
  }

  public BigDecimal getValue() {
    try {
    if (getFormula() != null && !formula.isEmpty())
      value = Calculator.getOperationResult(formula);
    return value;
    } catch (NumberFormatException e) {
      return new BigDecimal(-1);
    }
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public void setCategory(CategoryEntity category) {
    this.category = category;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public String getComment() {
    if (encoded) decode();
    return comment;
  }

  public void setComment(String comment) {
    this.comment_byte = Encryption.encryption(comment);
    this.comment = comment;
  }

  public boolean isNotify() {
    return notify;
  }

  public void setNotify(boolean notify) {
    this.notify = notify;
  }

  public LocalDate getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(LocalDate updateDate) {
    this.updateDate = updateDate;
  }

  public ExpenseSheetEntity getExpenseSheet() {
    return expenseSheet;
  }

  public void setExpenseSheet(ExpenseSheetEntity expenseSheet) {
    this.expenseSheet = expenseSheet;
  }

  @Override
  public String toString() {
    if (encoded)
      return getClass().getSimpleName()+"[" + getDate() + "]";
    else
      return getClass().getSimpleName()+"[" + getDate() + ";" + getCategory() + ";" + getValue() + "]";
  }

}
