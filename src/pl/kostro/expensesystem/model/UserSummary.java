package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserSummary extends AbstractEntity {

  private static final long serialVersionUID = 4742335081341225841L;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  private int id;
  @ManyToOne
  private UserLimit userLimit;
  @Column(name="s_date")
  private Date date;
  private BigDecimal limit;
  private BigDecimal exSummary;
  
  public UserSummary() {
    super();
  }

  public UserSummary(UserLimit userLimit, Date date, BigDecimal limit) {
    this.userLimit = userLimit;
    this.date = date;
    this.limit = limit;
    this.exSummary = new BigDecimal(0);
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  
  public UserLimit getUserLimit() {
    return userLimit;
  }
  
  public void setUserLimit(UserLimit userLimit) {
    this.userLimit = userLimit;
  }
  
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public BigDecimal getLimit() {
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }
  
  public BigDecimal getExSummary() {
    return exSummary;
  }

  public void setExSummary(BigDecimal exSummary) {
    this.exSummary = exSummary;
  }

  public String toString() {
    return userLimit.getUser().getName() + " " + date;
  }

}
