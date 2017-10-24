package pl.kostro.expensesystem.model.mongodb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.utils.Encryption;

import com.vaadin.server.VaadinSession;

@SuppressWarnings("serial")
public class NewUserLimit extends NewAbstractEntity {
  @Id
  private String id;

  private NewUser user;
  @Transient
  private BigDecimal limit;

  private byte[] limit_byte;

  private int order;

  private List<NewUserSummary> userSummaryList;

  private boolean continuousSummary;

  public NewUserLimit() {
    super();
  }

  public NewUserLimit(NewUser user, int order) {
    this.user = user;
    setLimit(new BigDecimal(0));
    this.order = order;
  }

  public NewUserLimit(UserLimit userLimit, NewRealUser newRealUser, NewUser newUser) {
    if (newRealUser != null)
      this.user = newRealUser;
    else if (newUser != null)
      this.user = newUser;
    this.limit_byte = userLimit.getLimitByte();
    this.order = userLimit.getOrder();
    this.continuousSummary = userLimit.isContinuousSummary();
    
  }
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public NewUser getUser() {
    return user;
  }

  public void setUser(NewUser user) {
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

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public List<NewUserSummary> getUserSummaryList() {
    if (userSummaryList == null)
      userSummaryList = new ArrayList<NewUserSummary>();
    return userSummaryList;
  }

  public void setUserSummaryList(List<NewUserSummary> userSummaryList) {
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
