package de.nixis.kk.helpers.env;

import de.nixis.kk.Server;
import de.nixis.kk.data.ServerOptions;
import de.nixis.kk.helpers.Client;
import de.nixis.kk.helpers.ClientOptions;
import org.junit.ClassRule;
import org.junit.Rule;


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
  protected void setup(ServerOptions serverOptions) {

    super.setup(serverOptions);

    ClientOptions clientOptions = ClientOptions.fromOptions(serverOptions);

    server = createServer(serverOptions);
    client = createClient(clientOptions);

    if (startServer) {
      server.run();
    }
  }

  @Override
  protected void tearDown() {
    super.tearDown();

    server.stop();
  }

  protected Server createServer(ServerOptions serverOptions) {
    return new Server(serverOptions);
  }

  protected Client createClient(ClientOptions clientOptions) {
    return new Client(clientOptions);
  }

  public Client client() {
    return client;
  }

  public Server server() {
    return server;
  }

}
