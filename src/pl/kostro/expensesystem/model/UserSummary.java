package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="users_summaries")
public class UserSummary extends AbstractEntity {

  private static final long serialVersionUID = 4742335081341225841L;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name="us_id")
  private int id;
  @Column(name="us_date")
  private Date date;
  @Column(name="us_limit")
  private BigDecimal limit;
  @Column(name="us_sum")
  private BigDecimal sum;
  
  public UserSummary() {
    super();
  }

  public UserSummary(Date date, BigDecimal limit) {
    this.date = date;
    this.limit = limit;
    this.sum = new BigDecimal(0);
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
    return date + " " + limit + " " + sum;
  }
  
  @Override
  public boolean equals(Object o) {
    if(o instanceof UserSummary)
      return getId() == ((UserSummary)o).getId();
    else return this == o;
  }

}
