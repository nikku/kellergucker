package de.nixis.kk;

import com.google.api.client.http.HttpResponse;
import de.nixis.kk.data.ServerOptions;
import helpers.Client;
import helpers.ClientOptions;
import helpers.Ports;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author nikku
 */
public class ServerTest {

  @Test
  public void shouldStart() throws Exception {

    // given
    int port = Ports.getPort();

    ServerOptions serverOptions = ServerOptions.fromEnvironment().setPort(port);

    ClientOptions clientOptions = ClientOptions.fromOptions(serverOptions);

    Server server = new Server(serverOptions);
    Client client = new Client(clientOptions);

    // when
    server.run();

    // then
    HttpResponse response = client.query("GET /");

    try {
      assertThat(response.getStatusCode()).isEqualTo(200);
    } finally {
      server.stop();
    }
  }
  
}
