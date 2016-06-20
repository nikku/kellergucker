package de.nixis.kk.data;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class MailerOptions {

  private String username;
  private String password;
  private String host;
  private int port;
  private boolean tls = false;


  //////// helpers //////////////////////////////////////

  public static MailerOptions fromEnvironment() {

    String host = System.getenv("MAILER_HOST");
    String username = System.getenv("MAILER_USERNAME");
    String password = System.getenv("MAILER_PASSWORD");

    int port = 25;
    boolean tls = Boolean.parseBoolean(System.getenv("MAILER_USE_TLS"));

    try {
      port = Integer.parseInt(System.getenv("MAILER_PORT"));
    } catch (NumberFormatException e) {
      // ignore
    }

    return
      new MailerOptions()
        .setHost(host)
        .setPort(port)
        .setTls(true)
        .setUsername(username)
        .setPassword(password);
  }

}
