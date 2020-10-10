package pl.kostro.expensesystem.db.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.LocalDatePersistenceConverter;

@SuppressWarnings("serial")
@Entity
@Table(name = "users_summaries")
public class UserSummaryEntity extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "us_id")
  private Long id;
  @Column(name = "us_date")
  @Convert(converter = LocalDatePersistenceConverter.class)
  private LocalDate date;
  @Column(name = "us_limit_byte")
  private byte[] limit_byte;
  @Column(name = "us_sum_byte")
  private byte[] sum_byte;

  public UserSummaryEntity() {
    super();
  }

  public UserSummaryEntity(LocalDate date, byte[] limit_byte, byte[] sum_byte) {
    this.date = date;
    this.limit_byte = limit_byte;
    this.sum_byte = sum_byte;
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

  public byte[] getLimitByte() {
    return limit_byte;
  }

  public void setLimitByte(byte[] limit_byte) {
    this.limit_byte = limit_byte;
  }

  public byte[] getSumByte() {
    return sum_byte;
  }

  public void setSumByte(byte[] sum_byte) {
    this.sum_byte = sum_byte;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getDate()+"; "+getLimitByte()+"; "+getSumByte()+"]";
  }
}
