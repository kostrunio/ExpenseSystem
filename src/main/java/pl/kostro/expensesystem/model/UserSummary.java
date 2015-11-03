package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.Encryption;

import com.vaadin.server.VaadinSession;

@Entity
@Table(name="users_summaries")
public class UserSummary extends AbstractEntity {

  private static final long serialVersionUID = 4742335081341225841L;

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name="us_id")
  private int id;
  @Column(name="us_date")
  private Date date;
  @Column(name="us_limit")
  private BigDecimal limit;
  @Column(name="us_limit_byte")
  private byte[] limit_byte;
  @Column(name="us_sum")
  private BigDecimal sum;
  @Column(name="us_sum_byte")
  private byte[] sum_byte;
  
  public UserSummary() {
    super();
  }

  public UserSummary(Date date, BigDecimal limit) {
    this.date = date;
    setLimit(limit);
    setSum(new BigDecimal(0));
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public BigDecimal getLimit() {
    if (limit_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      limit = new BigDecimal(enc.decryption(limit_byte));
    }
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    if (limit_byte != null && limit.equals(this.limit)) return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.limit_byte = enc.encryption(limit.toString());
    this.limit = limit;
  }
  
  public BigDecimal getSum() {
    if (sum_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      sum = new BigDecimal(enc.decryption(sum_byte));
    }
    return sum;
  }

  public void setSum(BigDecimal sum) {
    if (sum_byte != null && sum.equals(this.sum)) return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.sum_byte = enc.encryption(sum.toString());
    this.sum = sum;
  }

  @Override
  public String toString() {
    return date + " " + limit + " " + sum;
  }
  
  @Override
  public boolean equals(Object o) {
    if(o instanceof UserSummary)
      return getId() == ((UserSummary)o).getId();
    else return this == o;
  }

}
