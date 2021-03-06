package pl.kostro.expensesystem.model.entity;

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

import pl.kostro.expensesystem.utils.encryption.Encryption;

@Entity
@Table(name = "users_limits")
public class UserLimitEntity extends AbstractEntity {
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Column(name = "ul_id")
  private Long id;
  @OneToOne
  @JoinColumn(name = "ul_u_id")
  private UserEntity user;
  @Transient
  private BigDecimal limit;
  @Column(name = "ul_limit_byte")
  private byte[] limit_byte;
  @Column(name = "ul_order")
  private int order;
  @OneToMany(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "us_ul_id")
  @OrderBy(value = "date")
  private List<UserSummaryEntity> userSummaryList;
  @Column(name = "ul_cont_summary")
  private boolean continuousSummary;

  public UserLimitEntity() {
    super();
  }

  public UserLimitEntity(UserEntity user, int order) {
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

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
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

  public List<UserSummaryEntity> getUserSummaryList() {
    if (userSummaryList == null)
      userSummaryList = new ArrayList<>();
    return userSummaryList;
  }

  public void setUserSummaryList(List<UserSummaryEntity> userSummaryList) {
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
