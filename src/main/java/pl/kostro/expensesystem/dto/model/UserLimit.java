package pl.kostro.expensesystem.dto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserLimit {
  private Long id;
  private User user;
  private BigDecimal limit;
  private int order;
  private List<UserSummary> userSummaryList;
  private boolean continuousSummary;

  public UserLimit() {
  }

  public UserLimit(User user, int order) {
    this.user = user;
    this.limit = new BigDecimal(0);
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

  @Override
  public String toString() {
    return getClass().getSimpleName()+"["+getUser().getName()+"]";
  }

}
