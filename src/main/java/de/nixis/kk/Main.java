package de.nixis.kk;

import de.nixis.kk.data.ServerOptions;
import helpers.util.Migrations;


/**
 *
 * @author nikku
 */
public class Main {

  public static void main(String[] args) {

    ServerOptions options = ServerOptions.fromEnvironment();

    System.out.println(
      "\n *********************************************" +
      "\n *" +
      "\n *  Kellergucker App" +
      "\n *" +
      "\n *  DB_URL: " + options.getJdbcUrl() +
      "\n *  DB_MIGRATE: " + options.isMigrate() +
      "\n *" +
      "\n *  PORT: " + options.getPort() +
      "\n *  PRODUCTION: " + options.isProduction() +
      "\n *  ADMIN_KEY: " + options.getAdminKey() +
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
