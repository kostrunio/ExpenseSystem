package pl.kostro.expensesystem.utils.mail;

import lombok.extern.slf4j.Slf4j;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;
import pl.kostro.expensesystem.model.entity.RealUserEntity;
import pl.kostro.expensesystem.utils.msg.Msg;

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
import java.text.MessageFormat;
import java.util.Properties;

@Slf4j
public class SendEmail {

  public static void welcome(RealUserEntity user) {
    if (user.getEmail().isEmpty()) return;
    try {
      Message message = new MimeMessage(prepareSession());
      message.setFrom(new InternetAddress("ExpenseSystem <expense_system@mailplus.pl>"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getName() + "<" + user.getEmail() + ">"));
      
      message.setSubject(Msg.get("email.welcome.subject"));
      Multipart multipart = new MimeMultipart();
      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setText(Msg.get("email.welcome.text"), "UTF-8");
      multipart.addBodyPart(messageBodyPart);
      
      messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(Msg.get("email.welcome.path"));
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(Msg.get("email.welcome.fileName"));
      multipart.addBodyPart(messageBodyPart);
      message.setContent(multipart);

      log.info("SendEmail: Sending");
      Transport.send(message);
      log.info("SendEmail: Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static void expenses(RealUserEntity user, ExpenseSheetEntity expenseSheet, int expenses) {
    if (user.getEmail().isEmpty()) return;
    try {
      Message message = new MimeMessage(prepareSession());
      message.setFrom(new InternetAddress("ExpenseSystem <expense_system@mailplus.pl>"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getName() + "<" + user.getEmail() + ">"));
      
      message.setSubject(Msg.get("email.expenses.subject"));
      message.setContent(
          MessageFormat.format(Msg.get("email.expenses.text"),
              expenseSheet.getId(), expenseSheet.getName(), expenses),
          "text/html; charset=UTF-8");

      log.info("SendEmail: Sending");
      Transport.send(message);
      log.info("SendEmail: Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static Session prepareSession() {
    Properties props = new Properties();
    props.put("mail.smtp.host", System.getenv("MAIL_HOST"));
    props.put("mail.smtp.socketFactory.port", System.getenv("MAIL_PORT"));
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", System.getenv("MAIL_PORT"));
    return Session.getDefaultInstance(props, new javax.mail.Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(System.getenv("MAIL_USERNAME"), System.getenv("MAIL_PASSWORD"));
      }
    });
  }

  public static void exception(String exception, StackTraceElement[] stackTraceElements) {
    try {
      Message message = new MimeMessage(prepareSession());
      message.setFrom(new InternetAddress("ExpenseSystem <expense_system@mailplus.pl>"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("Admin" + "<" + "michal.kostro@10g.pl" + ">"));
      
      message.setSubject(Msg.get("email.exception.subject"));
      message.setContent(
          MessageFormat.format(Msg.get("email.exception.text"),
              exception, stackTraceElements.toString()),
          "text/html");

      log.info("SendEmail: Sending");
      Transport.send(message);
      log.info("SendEmail: Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

}