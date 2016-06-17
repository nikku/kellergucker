package de.nixis.kk;

import de.nixis.kk.data.MailerOptions;
import de.nixis.kk.data.ServerOptions;
import de.nixis.kk.helpers.util.Migrations;


/**
 *
 * @author nikku
 */
public class Main {

  public static void main(String[] args) {

    ServerOptions options = ServerOptions.fromEnvironment();

    MailerOptions mailerOptions = options.getMailerOptions();

    System.out.println(
      "\n *********************************************" +
      "\n *" +
      "\n *  Kellergucker App" +
      "\n *" +
      "\n *  DB_URL: " + options.getJdbcUrl() +
      "\n *  DB_MIGRATE: " + options.isMigrate() +
      "\n *" +
      "\n *  PORT: " + options.getPort() +
      "\n *  CACHE_TEMPLATES: " + options.isCacheTemplates() +
      "\n *  ADMIN_KEY: " + options.getAdminKey() +
      "\n *" +
      "\n *  MAILER: " + (
            mailerOptions != null ?
              "(host: " + mailerOptions.getHost() + ", username: " + mailerOptions.getUsername() + ", useTLS: " + mailerOptions.isUseTLS() + ")" :
              "NULL"
            ) +
      "\n *" +
      "\n *********************************************" +
      "\n"
    );

    if (options.isMigrate()) {
      new Migrations(options.getJdbcUrl()).migrate();
    }

    new Server(options).run();
  }

}
