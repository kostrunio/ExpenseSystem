package pl.kostro.expensesystem.dto.model;

import com.vaadin.server.VaadinSession;
import org.hibernate.annotations.GenericGenerator;
import pl.kostro.expensesystem.utils.Encryption;
import pl.kostro.expensesystem.utils.LocalDatePersistenceConverter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UserSummary {
  private Long id;
  private LocalDate date;
  private BigDecimal limit;
  private BigDecimal sum;

  public UserSummary() {
  }

  public UserSummary(LocalDate date, BigDecimal limit) {
    this.date = date;
    this.limit = limit;
    this.sum = new BigDecimal(0);
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

  public BigDecimal getLimit() {
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public void setSum(BigDecimal sum) {
    this.sum = sum;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getDate()+"; "+getLimit()+"; "+getSum()+"]";
  }
}
