package pl.kostro.expensesystem.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import pl.kostro.expensesystem.utils.Encryption;

import com.vaadin.server.VaadinSession;

@Entity
@Table(name="users_limits")
public class UserLimit extends AbstractEntity {

  private static final long serialVersionUID = 3781249751926840458L;

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name="ul_id")
  private int id;
  @OneToOne
  @JoinColumn(name="ul_u_id")
  private User user;
  @Column(name="ul_limit")
  private BigDecimal limit;
  @Column(name="ul_limit_byte")
  private byte[] limit_byte;
  @Column(name="ul_order")
  private int order;
  @OneToMany(cascade=CascadeType.REMOVE)
  @JoinColumn(name="us_ul_id")
  @OrderBy(value="date")
  private List<UserSummary> userSummaryList;
  @Column(name="ul_cont_summary")
  private boolean continuousSummary;
  
  public UserLimit() {
    super();
  }

  public UserLimit(User user, int order) {
    this.user = user;
    setLimit(new BigDecimal(0));
    this.order = order;
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
    if (limit == null && limit_byte != null) {
      Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
      limit = new BigDecimal(enc.decryption(limit_byte));
    }
    if (limit != null && limit_byte == null)
      setLimit(limit);
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    if (limit_byte != null && limit.equals(this.limit)) return;
    Encryption enc = new Encryption(VaadinSession.getCurrent().getAttribute(ExpenseSheet.class).getKey());
    this.limit_byte = enc.encryption(limit.toString());
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
  
  @Override
  public int hashCode() {
	  int hash = id;
	  hash += user.hashCode();
	  hash += getLimit().hashCode();
	  hash += order;
	  return hash;
  }

}
