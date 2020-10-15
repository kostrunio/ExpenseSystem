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

@SuppressWarnings("serial")
@Entity
@Table(name = "users_limits")
public class UserLimit extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "ul_id")
  private Long id;
  @OneToOne
  @JoinColumn(name = "ul_u_id")
  private User user;
  @Transient
  private BigDecimal limit;
  @Column(name = "ul_limit_byte")
  private byte[] limit_byte;
  @Column(name = "ul_order")
  private int order;
  @OneToMany(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "us_ul_id")
  @OrderBy(value = "date")
  private List<UserSummary> userSummaryList;
  @Column(name = "ul_cont_summary")
  private boolean continuousSummary;

  public UserLimit() {
    super();
  }

  public UserLimit(User user, int order) {
    this.user = user;
    setLimit(new BigDecimal(0));
    this.order = order;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  public void setUser(String name) {
    this.user.setName(name);
  }

  public BigDecimal getLimit() {
    return getLimit(false);
  }

  public BigDecimal getLimit(boolean encrypt) {
    if (limit == null && limit_byte != null && !encrypt) {
      limit = new BigDecimal(Encryption.decryption(limit_byte));
    }
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    setLimit(limit, false);
  }

  public void setLimit(BigDecimal limit, boolean encrypt) {
    if (limit_byte != null && limit.equals(this.limit) && !encrypt)
      return;
    this.limit_byte = Encryption.encryption(limit.toString());
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

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getUser().getName()+"]";
  }

}
