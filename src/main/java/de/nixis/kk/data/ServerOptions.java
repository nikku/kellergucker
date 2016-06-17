package de.nixis.kk.data;

import de.nixis.kk.helpers.util.Validatable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author nikku
 */
@Data
@Accessors(chain = true)
public class ServerOptions implements Validatable {

  private int port = 8080;

  private boolean migrate = false;

  private boolean cacheTemplates = false;

  private String jdbcUrl;

  private String adminKey;

  private MailerOptions mailerOptions;

  @Override
  public boolean isValid() {
    return jdbcUrl != null;
  }


  ////// static helpers ////////////////////////////////

  public static final String DEFAULT_JDBC_URL = "jdbc:postgresql://localhost:5432/app?user=app";

  public static final int DEFAULT_PORT = 8080;


  public static ServerOptions fromEnvironment() {

    String jdbcUrl = System.getenv("DB_URL");

    String adminKey = System.getenv("ADMIN_KEY");

    boolean migrate = Boolean.parseBoolean(System.getenv("DB_MIGRATE"));

    boolean cacheTemplates = Boolean.parseBoolean(System.getenv("CACHE_TEMPLATES"));

    int port = -1;

    String mailerHost = System.getenv("MAILER_HOST");
    String mailerUsername = System.getenv("MAILER_USERNAME");
    String mailerPassword = System.getenv("MAILER_PASSWORD");
    Boolean mailerUseTLS = Boolean.parseBoolean(System.getenv("MAILER_USE_TLS"));

    MailerOptions mailerOptions = null;

    try {
      port = Integer.parseInt(System.getenv("PORT"));
    } catch (NumberFormatException e) {
      ; // don't care
    }

    if (mailerHost != null) {
      mailerOptions =
          new MailerOptions()
              .setHost(mailerHost)
              .setUsername(mailerUsername)
              .setPassword(mailerPassword)
              .setUseTLS(mailerUseTLS);
    }

    return new ServerOptions()
        .setMailerOptions(mailerOptions)
        .setJdbcUrl(jdbcUrl == null ? DEFAULT_JDBC_URL : jdbcUrl)
        .setAdminKey(adminKey)
        .setPort(port != -1 ? port : DEFAULT_PORT)
        .setCacheTemplates(cacheTemplates)
        .setMigrate(migrate);
  }

}
