package helpers.util;

import org.flywaydb.core.Flyway;

/**
 *
 * @author nikku
 */
public class Migrations {

  private final Flyway flyway;

  public Migrations(String jdbcUrl) {
    flyway = new Flyway();

    flyway.setDataSource(jdbcUrl, null, null);
  }

  public Migrations migrate() {
    flyway.migrate();

    return this;
  }

  public Migrations clean() {
    flyway.clean();

    return this;
  }

  public Migrations setLocations(String... locations) {
    flyway.setLocations(locations);

    return this;
  }
}
