package pl.kostro.expensesystem.model.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;
import pl.kostro.expensesystem.model.repository.RealUserRepository;

@Service
public class RealUserService {
  
  @Autowired
  private RealUserRepository rur;

  static MessageDigest messageDigest;

  static {
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
    }
  }

  public RealUser createRealUser(String name, String password, String email) {
    RealUser realUser = new RealUser();
    realUser.setName(name);
    messageDigest.update(password.getBytes());
    realUser.setPassword(new String(messageDigest.digest()));
    realUser.setEmail(email);
    rur.save(realUser);
    return realUser;
  }

  public void merge(RealUser realUser, boolean passwordChange) {
    if (passwordChange) {
      messageDigest.update(realUser.getClearPassword().getBytes());
      realUser.setPassword(new String(messageDigest.digest()));
    }
    rur.save(realUser);
  }

  public RealUser refresh(RealUser realUser) {
    return rur.findOne(realUser.getId());
  }

  public void setDefaultExpenseSheet(RealUser realUser, ExpenseSheet expenseSheet) {
    realUser.setDefaultExpenseSheet(expenseSheet);
    rur.save(realUser);
  }

  public RealUser getUserData(String userName, String password) {
    messageDigest.update(password.getBytes());
    RealUser loggedUser = rur.findByNameAndPassword(userName, new String(messageDigest.digest()));
    if (loggedUser == null) return null; 
    loggedUser.setClearPassword(password);
    loggedUser.setLogDate(new Date());
    if (loggedUser.getPasswordByte() == null)
      loggedUser.setPasswordByte(messageDigest.digest());
    rur.save(loggedUser);
    return loggedUser;
  }

  public RealUser findRealUser(String userName) {
    RealUser loggedUser = null;
    try {
      loggedUser = rur.findByName(userName);
    } catch (NoResultException e) {

    }
    return loggedUser;
  }

  public void fetchExpenseSheetList(RealUser realUser) {
    realUser.setExpenseSheetList(rur.fetchExpenseSheetList(realUser));
  }
}
