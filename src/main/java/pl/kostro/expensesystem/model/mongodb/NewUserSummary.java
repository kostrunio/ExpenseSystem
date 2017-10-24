package pl.kostro.expensesystem.model.mongodb;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import pl.kostro.expensesystem.model.UserSummary;
import pl.kostro.expensesystem.utils.Encryption;

import com.vaadin.server.VaadinSession;

@SuppressWarnings("serial")
public class NewUserSummary extends NewAbstractEntity {
  @Id
  private String id;

  private LocalDate date;
  @Transient
  private BigDecimal limit;

  private byte[] limit_byte;
  @Transient
  private BigDecimal sum;

  private byte[] sum_byte;

  public NewUserSummary() {
    super();
  }

  public NewUserSummary(LocalDate date, BigDecimal limit) {
    this.date = date;
    setLimit(limit);
    setSum(new BigDecimal(0));
  }

  public NewUserSummary(UserSummary userSummary) {
    this.date = userSummary.getDate();
    this.limit_byte = userSummary.getLimitByte();
    this.sum_byte = userSummary.getSumByte();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public BigDecimal getLimit() {
    return getLimit(false);
  }

  public BigDecimal getLimit(boolean encrypt) {
    if (limit == null && limit_byte != null && !encrypt) {
      try {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
      limit = new BigDecimal(enc.decryption(limit_byte));
      } catch (NullPointerException e) {
        return new BigDecimal("-1");
      }
    }
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    setLimit(limit, false);
  }

  public void setLimit(BigDecimal limit, boolean encrypt) {
    if (limit_byte != null && limit.equals(this.limit) && !encrypt)
      return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
    this.limit_byte = enc.encryption(limit.toString());
    this.limit = limit;
  }

  public BigDecimal getSum() {
    return getSum(false);
  }

  public BigDecimal getSum(boolean encrypt) {
    if (sum == null && sum_byte != null && !encrypt) {
      try {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
      sum = new BigDecimal(enc.decryption(sum_byte));
      } catch (NullPointerException e) {
        return new BigDecimal("-1");
      }
    }
    return sum;
  }

  public void setSum(BigDecimal sum) {
    setSum(sum, false);
  }

  public void setSum(BigDecimal sum, boolean encrypt) {
    if (sum_byte != null && sum.equals(this.sum) && !encrypt)
      return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(NewExpenseSheet.class).getKey());
    this.sum_byte = enc.encryption(sum.toString());
    this.sum = sum;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getDate()+"; "+getLimit()+"; "+getSum()+"]";
  }
}
