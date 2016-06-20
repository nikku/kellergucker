
package de.nixis.kk.logic;

import de.nixis.kk.data.MailerOptions;
import de.nixis.kk.data.notifications.Identity;
import de.nixis.kk.data.notifications.Mail;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Mailer {

  private static final Identity DEFAULT_SENDER =
          Identity.create("noreply@kellergucker", "Kellergucker");

  private final MailerOptions options;

  public Mailer(MailerOptions options) {
    this.options = options;
  }

  public void sendMail(Mail mail) {

    Identity receiver = mail.getReceiver();
    Identity sender = mail.getSender();

    if (receiver == null) {
      throw new IllegalArgumentException("no receiver");
    }

    if (sender == null) {
      sender = DEFAULT_SENDER;
    }

    String username = options.getUsername();
    String password = options.getPassword();

    Properties props = new Properties();

    props.put("mail.debug", "true");
    props.put("mail.smtp.host", options.getHost());
    props.put("mail.smtp.port", options.getPort());

    Authenticator authenticator = null;

    if (username != null) {
      props.put("mail.smtp.auth", "true");

      authenticator = new PasswordAuthenticator(username, password);
    }

    props.put("mail.smtp.starttls.enable", "true");

    // always use TLS ?
    if (options.isTls()) {
      props.put("mail.smtp.starttls.required", "true");
    }

    Session session = Session.getDefaultInstance(props, authenticator);

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(sender.toAdress());
      msg.addRecipient(Message.RecipientType.TO, receiver.toAdress());

      msg.setSubject(mail.getSubject());

      MimeMultipart multipart = new MimeMultipart();
      MimeBodyPart plainPart = new MimeBodyPart();
      plainPart.setContent(mail.getBody(), "text/plain; charset=utf-8");
      multipart.addBodyPart(plainPart);

      msg.setContent(multipart);

      Transport.send(msg);
    } catch (AddressException e) {
      throw new RuntimeException(e);
    } catch (MessagingException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

  }


  ///////// helpers ///////////////////////////////////

  private static class PasswordAuthenticator extends Authenticator {

    private final String username;
    private final String password;

    public PasswordAuthenticator(String username, String password) {
      this.username = username;
      this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(username, password);
    }
  }

}
