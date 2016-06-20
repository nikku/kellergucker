package de.nixis.kk.logic;

import static de.nixis.kk.helpers.Ports.getPort;
import static org.assertj.core.api.Assertions.assertThat;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import de.nixis.kk.data.MailerOptions;
import de.nixis.kk.data.notifications.Identity;
import de.nixis.kk.data.notifications.Mail;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.assertj.core.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MailerTest {

  private static GreenMail mailBackend;

  private static Mailer mailer;

  @BeforeClass
  public static void beforeClass() throws Exception {
    MailerOptions mailerOptions =
        new MailerOptions()
          .setHost("localhost")
          .setPort(getPort());

    ServerSetup testSetup = new ServerSetup(mailerOptions.getPort(), "localhost", "smtp");

    mailer = new Mailer(mailerOptions);
    mailBackend = new GreenMail(testSetup);
  }

  @Before
  public void setup() {
    mailBackend.reset();
  }

  @After
  public void after() {
    mailBackend.stop();
  }

  @Test
  public void shouldSendMail() throws Exception {

    // given
    String body = "BAR YEA YEA!";
    String subject = "FOO";

    Identity sender = Identity.create("foo@bar", "FOO");
    Identity receiver = Identity.create("bbaba@ba", "FOO");

    Mail mail =
          new Mail()
            .setSender(sender)
            .setReceiver(receiver)
            .setSubject(subject)
            .setBody(body);

    // when
    mailer.sendMail(mail);

    // then
    MimeMessage receivedMessage = mailBackend.getReceivedMessages()[0];

    assertThat(receivedMessage.getFrom()).isEqualTo(Arrays.array(sender.toAdress()));
    assertThat(receivedMessage.getRecipients(Message.RecipientType.TO)).isEqualTo(Arrays.array(receiver.toAdress()));

    assertThat(receivedMessage.getSubject()).isEqualTo(subject);

    MimeMultipart multipart = (MimeMultipart) receivedMessage.getContent();
    BodyPart bodyPart = multipart.getBodyPart(0);

    assertThat(bodyPart.getContent()).isEqualTo(body);
    assertThat(bodyPart.getContentType()).isEqualTo("text/plain; charset=utf-8");
  }


  @Test
  public void shouldSendMailWithoutSender() throws Exception {

    // given
    String body = "BAR YEA YEA!";
    String subject = "FOO";

    Identity receiver = Identity.create("bbaba@ba", "FOO");
    Identity sender = Identity.create("noreply@kellergucker", "Kellergucker");

    Mail mail =
          new Mail()
            .setReceiver(receiver)
            .setSubject(subject)
            .setBody(body);

    // when
    mailer.sendMail(mail);

    // then
    MimeMessage receivedMessage = mailBackend.getReceivedMessages()[0];

    assertThat(receivedMessage.getFrom()).isEqualTo(Arrays.array(sender.toAdress()));
    assertThat(receivedMessage.getRecipients(Message.RecipientType.TO)).isEqualTo(Arrays.array(receiver.toAdress()));
  }

}
