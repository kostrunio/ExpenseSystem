package pl.kostro.expensesystem.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pl.kostro.expensesystem.model.RealUser;

//File Name SendEmail.java

public class SendEmail {
  public static void welcome(RealUser user) {
    try {
      Message message = new MimeMessage(prepareSession());
      message.setFrom(new InternetAddress("ExpenseSystem <expense_system@mailplus.pl>"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getName() + "<" + user.getEmail() + ">"));
      
      message.setSubject("Testing Subject");
      message.setText("Test Mail");

      Transport.send(message);

      System.out.println("Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static Session prepareSession() {
    Properties props = new Properties();
    props.put("mail.smtp.host", "poczta.o2.pl");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("userName", "password");
      }
    });
    return session;
  }
}