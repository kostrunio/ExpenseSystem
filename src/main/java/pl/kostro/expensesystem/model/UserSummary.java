package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.Encryption;

import com.vaadin.server.VaadinSession;

@SuppressWarnings("serial")
@Entity
@Table(name = "users_summaries")
public class UserSummary extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "us_id")
  private Long id;
  @Column(name = "us_date")
  private Date date;
  @Transient
  private BigDecimal limit;
  @Column(name = "us_limit_byte")
  private byte[] limit_byte;
  @Transient
  private BigDecimal sum;
  @Column(name = "us_sum_byte")
  private byte[] sum_byte;

  public UserSummary() {
    super();
  }

  public UserSummary(Date date, BigDecimal limit) {
    this.date = date;
    setLimit(limit);
    setSum(new BigDecimal(0));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public BigDecimal getLimit() {
    return getLimit(false);
  }

  public BigDecimal getLimit(boolean encrypt) {
    if (limit == null && limit_byte != null && !encrypt) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      limit = new BigDecimal(enc.decryption(limit_byte));
    }
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    setLimit(limit, false);
  }

  public void setLimit(BigDecimal limit, boolean encrypt) {
    if (limit_byte != null && limit.equals(this.limit) && !encrypt)
      return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.limit_byte = enc.encryption(limit.toString());
    this.limit = limit;
  }

  public BigDecimal getSum() {
    return getSum(false);
  }

  public BigDecimal getSum(boolean encrypt) {
    if (sum == null && sum_byte != null && !encrypt) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      sum = new BigDecimal(enc.decryption(sum_byte));
    }
    return sum;
  }

  public void setSum(BigDecimal sum) {
    setSum(sum, false);
  }

  public void setSum(BigDecimal sum, boolean encrypt) {
    if (sum_byte != null && sum.equals(this.sum) && !encrypt)
      return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.sum_byte = enc.encryption(sum.toString());
    this.sum = sum;
  }

  @Override
  public String toString() {
    return date + " " + limit + " " + sum;
  }

}
