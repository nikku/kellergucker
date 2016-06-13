package helpers;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

/**
 *
 * @author nikku
 */
public class Client {

  private final ClientOptions options;

  private final NetHttpTransport transport;
  private final HttpRequestFactory requestFactory;

  public Client(ClientOptions options) {
    this.options = options;
    
    this.transport = new NetHttpTransport();
    this.requestFactory = transport.createRequestFactory();
  }


  public HttpResponse query(String definition) throws IOException {
    return request(definition).execute();
  }

  public HttpRequest request(String definition) throws IOException {

    String[] defs = definition.split(" ");

    String method = null;
    String path = null;

    switch (defs.length) {
      case 1:
        method = "GET";
        break;
      case 2:
        method = defs[0];
        path = defs[1];
        break;
      default:
        throw new IllegalArgumentException("invalid request (expected <[METHOD] /some/url>)");
    }

    String baseUrl = "http://localhost:" + options.getPort();
    
    GenericUrl url = new GenericUrl((path.startsWith(baseUrl) ? "" : baseUrl)+ path);

    return requestFactory.buildRequest(method, url, null);
  }
  
}
