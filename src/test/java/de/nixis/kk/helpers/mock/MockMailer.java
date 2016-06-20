package de.nixis.kk.helpers.mock;

import de.nixis.kk.data.notifications.Mail;
import de.nixis.kk.logic.Mailer;
import java.util.ArrayList;
import java.util.List;


public class MockMailer extends Mailer {

  public MockMailer() {
    super(null);
  }

  private Mail lastSentMail;
  private List<Mail> sentMails = new ArrayList<>();

  public Mail getLastSentMail() {
    return lastSentMail;
  }

  public List<Mail> getSentMails() {
    return sentMails;
  }

  public void reset() {
    this.lastSentMail = null;
    this.sentMails.clear();
  }

  @Override
  public void sendMail(Mail mail) {
    this.lastSentMail = mail;

    this.sentMails.add(mail);
  }

}
