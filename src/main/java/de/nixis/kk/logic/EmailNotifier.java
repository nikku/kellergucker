
package de.nixis.kk.logic;

import de.nixis.kk.data.notifications.Identity;
import de.nixis.kk.data.notifications.Mail;
import de.nixis.kk.data.stocks.Recommendation;
import de.nixis.kk.data.user.User;
import de.nixis.kk.helpers.template.Templates;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;


public class EmailNotifier {

  private final Templates templates;
  private final Mailer mailer;

  public EmailNotifier(Templates templates, Mailer mailer) {
    this.templates = templates;
    this.mailer = mailer;
  }

  public void sendNotifications(Map<User, List<Recommendation>> recommendations) {

    recommendations.forEach((user, userRecommendations) -> {
      this.sendNotificationEmail(user, userRecommendations);
    });
  }

  public void sendNotificationEmail(User user, List<Recommendation> recommendations) {

    LoggerFactory.getLogger(this.getClass()).info("SENDING MAIL TO {}", user.getEmail());

    HashMap<String, Object> templateData = new HashMap<>();
    templateData.put("user", user);
    templateData.put("recommendations", recommendations);

    String email = templates.render(new ModelAndView(templateData, "email/recommendation"));


    String[] parts = email.split("\n*---\n*", 2);

    if (parts.length != 2) {
      throw new IllegalArgumentException("Expected {header}\\n---\\n{body} mail template");
    }

    Mail mail = new Mail()
            .setSubject(parts[0])
            .setBody(parts[1])
            .setReceiver(new Identity().setEmail(user.getEmail()).setName(user.getName()));

    mailer.sendMail(mail);
  }

}
