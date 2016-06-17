
package de.nixis.kk.logic;

import de.nixis.kk.data.MailerOptions;
import de.nixis.kk.data.stocks.Recommendation;
import de.nixis.kk.data.user.User;
import de.nixis.kk.helpers.template.Templates;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;


public class EmailNotifier {

  private final Templates templates;

  private final MailerOptions options;

  public EmailNotifier(Templates templates, MailerOptions options) {
    this.templates = templates;
    this.options = options;
  }

  public void sendNotificationEmail(User user, List<Recommendation> recommendations) {

    LoggerFactory.getLogger(this.getClass()).info("SENDING MAIL TO {}", user.getEmail());

    HashMap<String, Object> templateData = new HashMap<>();
    templateData.put("user", user);
    templateData.put("recommendations", recommendations);

    String email = templates.render(new ModelAndView(templateData, "email/recommendation"));

    String[] parts = email.split("---", 1);

    System.out.println(parts[0]);
    System.out.println(parts[1]);
    
    String username = options.getUsername();
    String password = options.getPassword();

    Properties props = new Properties();

    props.put("mail.debug", "true");
    props.put("mail.host", options.getHost());

    Authenticator authenticator = null;

    if (username != null) {
      props.put("mail.smtp.auth", "true");

      authenticator = new PasswordAuthenticator(username, password);
    }

    if (options.isUseTLS()) {
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.starttls.required", "true");
    }

    Session session = Session.getDefaultInstance(props, authenticator);

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress("mailer@nixis.de", "Nixis Mailer"));
      msg.addRecipient(Message.RecipientType.TO,
                       new InternetAddress(user.getEmail(), user.getName()));

      msg.setSubject(parts[0].trim());

      String plainBody = parts[1].trim();

      MimeMultipart multipart = new MimeMultipart();
      MimeBodyPart plainPart = new MimeBodyPart();
      plainPart.setContent(plainBody, "text/plain");
      multipart.addBodyPart(plainPart);

      msg.setContent(multipart);

      session.getTransport().send(msg);
    } catch (AddressException e) {
      throw new RuntimeException(e);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }


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
