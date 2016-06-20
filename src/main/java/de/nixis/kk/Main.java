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

    String configurationOutput =
      "\n *********************************************" +
      "\n *" +
      "\n *  Kellergucker App" +
      "\n *" +
      "\n *  DB_URL: %s" +
      "\n *  DB_MIGRATE: %s" +
      "\n *" +
      "\n *  PORT: %s" +
      "\n *  CACHE_TEMPLATES: %s" +
      "\n *  ADMIN_KEY: %s" +
      "\n *" +
      "\n *  MAILER_HOST: %s" +
      "\n *  MAILER_PORT: %s" +
      "\n *  MAILER_USERNAME: %s" +
      "\n *  MAILER_PASSWORD: %s" +
      "\n *" +
      "\n *********************************************" +
      "\n";

    System.out.println(
      String.format(configurationOutput,
        options.getJdbcUrl(),
        options.isMigrate(),
        options.getPort(),
        options.isCacheTemplates(),
        options.getAdminKey(),
        mailerOptions.getHost(),
        mailerOptions.getPort(),
        mailerOptions.getUsername(),
        mailerOptions.getPassword()
      )
    );

    if (options.isMigrate()) {
      new Migrations(options.getJdbcUrl()).migrate();
    }

    new Server(options).run();
  }

}
