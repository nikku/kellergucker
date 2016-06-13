package helpers;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author nikku
 */
public class Ports {

  /**
   * Returns a new free port on the system.
   * 
   * @return
   * @throws IOException 
   */
  public static int getPort() throws IOException {
    try (ServerSocket s = new ServerSocket(0)) {
      return s.getLocalPort();
    }
  }
}
