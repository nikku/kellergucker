package helpers.env;

import de.nixis.kk.data.ServerOptions;
import helpers.util.Migrations;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.sql2o.Sql2o;

import static helpers.Ports.getPort;

/**
 * A test environment that sets up database and migrations.
 *
 * Use as test rule, annotated as {@link ClassRule} or {@link Rule} in your test setup.
 *
 * @author nikku
 */
public class DatabaseEnvironment implements TestRule {

  private static final ServerOptionsProducer DEFAULT_OPTIONS_PRODUCER = () -> {
    return ServerOptions.fromEnvironment().setPort(getPort());
  };

  protected ServerOptionsProducer serverOptionsProducer = DEFAULT_OPTIONS_PRODUCER;

  protected boolean executeMigrations = false;

  private Sql2o db;

  private Migrations migrations;


  public DatabaseEnvironment() {
    this.serverOptionsProducer = DEFAULT_OPTIONS_PRODUCER;
  }

  @Override
  public Statement apply(Statement base, Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {
        setup();

        try {
          base.evaluate();
        } finally {
          tearDown();
        }
      }
    };
  }

  protected void setup() throws Exception {

    ServerOptions serverOptions = serverOptionsProducer.get();

    setup(serverOptions);
  }

  protected void setup(ServerOptions serverOptions) {

    db = new Sql2o(serverOptions.getJdbcUrl(), null, null);

    migrations = new Migrations(serverOptions.getJdbcUrl());

    migrations.clean();

    if (executeMigrations) {
      migrations.migrate();
    }
  }

  protected void tearDown() {
    migrations.clean();
  }

  public Migrations migrations() {
    return migrations;
  }

  public Sql2o db() {
    return db;
  }

  @FunctionalInterface
  public interface ServerOptionsProducer {

    ServerOptions get() throws Exception;
  }

}
