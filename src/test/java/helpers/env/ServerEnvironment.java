package helpers.env;

import de.nixis.kk.Server;
import de.nixis.kk.data.ServerOptions;
import helpers.Client;
import helpers.ClientOptions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A test environment that sets up database, migrations, client and server.
 *
 * Use as test rule, annotated as {@link ClassRule} or {@link Rule} in your test setup.
 *
 * @author nikku
 */
public class ServerEnvironment extends DatabaseEnvironment {

  protected boolean startServer = true;

  private Server server;
  private Client client;

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

  @Override
  protected void setup(ServerOptions serverOptions) {

    super.setup(serverOptions);

    ClientOptions clientOptions = ClientOptions.fromOptions(serverOptions);

    server = new Server(serverOptions);

    client = new Client(clientOptions);

    if (startServer) {
      server.run();
    }
  }

  @Override
  protected void tearDown() {
    super.tearDown();

    server.stop();
  }

  public Client client() {
    return client;
  }

  public Server server() {
    return server;
  }

}
