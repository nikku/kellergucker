package helpers;

import de.nixis.kk.data.ServerOptions;
import helpers.util.Migrations;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.sql2o.Sql2o;

/**
 *
 * @author nikku
 */
public class AbstractDbTest {

  protected static Sql2o db;

  protected static Migrations migrations;

  @BeforeClass
  public static void setupDatabase() throws Exception {
    String jdbcUrl = ServerOptions.fromEnvironment().getJdbcUrl();

    db = new Sql2o(jdbcUrl, null, null);
    
    migrations = new Migrations(jdbcUrl);
  }

  @AfterClass
  public static void afterClass() throws Exception {
    migrations.clean();
  }
  
}
