package helpers;

import de.nixis.kk.Server;
import de.nixis.kk.data.ServerOptions;
import helpers.util.Migrations;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author nikku
 */
public class AbstractServerTest {

  protected static Server server;

  protected static Client client;
  
  protected static Migrations migrations;


  @BeforeClass
  public static void beforeClass() throws Exception {

    int port = Ports.getPort();

    ServerOptions serverOptions = ServerOptions.fromEnvironment().setPort(port);
    
    ClientOptions clientOptions = ClientOptions.fromOptions(serverOptions);

    server = new Server(serverOptions);

    client = new Client(clientOptions);

    migrations = new Migrations(serverOptions.getJdbcUrl());

    server.run();
  }

  @AfterClass
  public static void afterClass() throws Exception {
    migrations.clean();
    
    server.stop();
  }
  
}
