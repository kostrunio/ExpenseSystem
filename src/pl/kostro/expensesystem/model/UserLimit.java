package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="users_limits")
public class UserLimit extends AbstractEntity {

  private static final long serialVersionUID = 3781249751926840458L;

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name="ul_id")
  private int id;
  @OneToOne
  @JoinColumn(name="ul_u_id")
  private User user;
  @Column(name="ul_limit")
  private BigDecimal limit;
  @Column(name="ul_order")
  private int order;
  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name="us_ul_id")
  @OrderBy(value="date")
  private List<UserSummary> userSummaryList;
  @Column(name="ul_cont_summary")
  private boolean continuousSummary;
  
  public UserLimit() {
    super();
  }

  public UserLimit(User user, BigDecimal limit) {
    this.user = user;
    this.limit = limit;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }

  public BigDecimal getLimit() {
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }
  
  public int getOrder() {
    return order;
  }
  
  public void setOrder(int order) {
    this.order = order;
  }
  
  public List<UserSummary> getUserSummaryList() {
    if (userSummaryList == null)
      userSummaryList = new ArrayList<UserSummary>();
    return userSummaryList;
  }

  public void setUserSummaryList(List<UserSummary> userSummaryList) {
    this.userSummaryList = userSummaryList;
  }
  
  public boolean isContinuousSummary() {
    return continuousSummary;
  }
  
  public void setContinuousSummary(boolean continuousSummary) {
    this.continuousSummary = continuousSummary;
  }

  public String toString() {
    return user.getName();
  }
  
  @Override
  public boolean equals(Object o) {
    if(o instanceof UserLimit)
      return getId() == ((UserLimit)o).getId();
    else return this == o;
  }

}
