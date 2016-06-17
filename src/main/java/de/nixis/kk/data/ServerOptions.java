package de.nixis.kk.data;

import helpers.util.Validatable;
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

    try {
      port = Integer.parseInt(System.getenv("PORT"));
    } catch (NumberFormatException e) {
      ; // don't care
    }

    return new ServerOptions()
        .setJdbcUrl(jdbcUrl == null ? DEFAULT_JDBC_URL : jdbcUrl)
        .setAdminKey(adminKey)
        .setPort(port != -1 ? port : DEFAULT_PORT)
        .setCacheTemplates(cacheTemplates)
        .setMigrate(migrate);
  }

}
