package pl.kostro.expensesystem.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.LocalDatePersistenceConverter;

@SuppressWarnings("serial")
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
  @Column(name = "e_formula_byte")
  private byte[] formula_byte;
  @OneToOne
  @JoinColumn(name = "e_c_id")
  private CategoryEntity category;
  @OneToOne
  @JoinColumn(name = "e_u_id")
  private UserEntity user;
  @Column(name = "e_comment_byte")
  private byte[] comment_byte;
  @Column(name = "e_notify")
  private boolean notify;
  @ManyToOne
  @JoinColumn(name = "e_es_id")
  private ExpenseSheetEntity expenseSheet;

  public ExpenseEntity() {
    super();
  }

  public ExpenseEntity(LocalDate date, byte[] formula_byte, CategoryEntity category, UserEntity user, byte[] comment_byte, boolean notify,
                       ExpenseSheetEntity expenseSheet) {
    this.date = date;
    this.formula_byte = formula_byte;
    this.category = category;
    this.user = user;
    this.comment_byte = comment_byte;
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

  public byte[] getFormulaByte() {
    return formula_byte;
  }

  public void setFormulaByte(byte[] formula_byte) {
    this.formula_byte = formula_byte;
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

  public byte[] getCommentByte() {
    return comment_byte;
  }

  public void setCommentByte(byte[] comment_byte) {
    this.comment_byte = comment_byte;
  }

  public boolean isNotify() {
    return notify;
  }

  public void setNotify(boolean notify) {
    this.notify = notify;
  }

  public ExpenseSheetEntity getExpenseSheet() {
    return expenseSheet;
  }

  public void setExpenseSheet(ExpenseSheetEntity expenseSheet) {
    this.expenseSheet = expenseSheet;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"[" + getDate() + ";" + getCategory() + "]";
  }

}
