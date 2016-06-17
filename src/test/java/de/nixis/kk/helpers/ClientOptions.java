package de.nixis.kk.helpers;

import de.nixis.kk.data.ServerOptions;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author nikku
 */
@Data
@Accessors(chain = true)
public class ClientOptions {

  private int port;


  /////// helpers ////////////////////////

  public static ClientOptions fromOptions(ServerOptions options) {
    return new ClientOptions().setPort(options.getPort());
  }

}
