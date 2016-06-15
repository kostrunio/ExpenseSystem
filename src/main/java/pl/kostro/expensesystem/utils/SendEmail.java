package pl.kostro.expensesystem.utils;

import java.text.MessageFormat;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import pl.kostro.expensesystem.Msg;
import pl.kostro.expensesystem.model.ExpenseSheet;
import pl.kostro.expensesystem.model.RealUser;

public class SendEmail {
  public static void welcome(RealUser user) {
    if (user.getEmail().isEmpty()) return;
    try {
      Message message = new MimeMessage(prepareSession());
      message.setFrom(new InternetAddress("ExpenseSystem <expense_system@mailplus.pl>"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getName() + "<" + user.getEmail() + ">"));
      
      message.setSubject(Msg.get("email.welcome.subject"));
      message.setContent(Msg.get("email.welcome.text"), "text/html");
      
      MimeBodyPart messageBodyPart = new MimeBodyPart();
      Multipart multipart = new MimeMultipart();
      messageBodyPart = new MimeBodyPart();
      String file = Msg.get("email.welcome.patch");
      String fileName = Msg.get("email.welcome.fileName");
      DataSource source = new FileDataSource(file);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(fileName);
      multipart.addBodyPart(messageBodyPart);
      message.setContent(multipart);

      System.out.println("SendEmail: Sending");

      Transport.send(message);

      System.out.println("SendEmail: Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static void expenses(RealUser user, ExpenseSheet expenseSheet, int expenses) {
    if (user.getEmail().isEmpty()) return;
    try {
      Message message = new MimeMessage(prepareSession());
      message.setFrom(new InternetAddress("ExpenseSystem <expense_system@mailplus.pl>"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getName() + "<" + user.getEmail() + ">"));
      
      message.setSubject(Msg.get("email.expenses.subject"));
      message.setContent(
          MessageFormat.format(Msg.get("email.expenses.text"),
              new Object[] {expenseSheet.getId(), expenseSheet.getName(), expenses}),
          "text/html");

      System.out.println("SendEmail: Sending");

      Transport.send(message);

      System.out.println("SendEmail: Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static Session prepareSession() {
    Properties props = new Properties();
    props.put("mail.smtp.host", System.getenv("OPENSHIFT_MAIL_HOST"));
    props.put("mail.smtp.socketFactory.port", System.getenv("OPENSHIFT_MAIL_PORT"));
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", System.getenv("OPENSHIFT_MAIL_PORT"));
    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(System.getenv("OPENSHIFT_MAIL_USERNAME"), System.getenv("OPENSHIFT_MAIL_PASSWORD"));
      }
    });
    return session;
  }

  public static void exception(String exception, StackTraceElement[] stackTraceElements) {
    try {
      Message message = new MimeMessage(prepareSession());
      message.setFrom(new InternetAddress("ExpenseSystem <expense_system@mailplus.pl>"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("Admin" + "<" + "michal.kostro@10g.pl" + ">"));
      
      message.setSubject(Msg.get("email.exception.subject"));
      message.setContent(
          MessageFormat.format(Msg.get("email.exception.text"),
              new Object[] {exception, stackTraceElements.toString()}),
          "text/html");

      System.out.println("SendEmail: Sending");

      Transport.send(message);

      System.out.println("SendEmail: Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}