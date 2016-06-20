package de.nixis.kk.data.notifications;

import java.io.UnsupportedEncodingException;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Identity {

  private String name;
  private String email;

  public Address toAdress() throws UnsupportedEncodingException {
    return new InternetAddress(email, name);
  }

  
  /////// helpers ////////////////////////////

  public static Identity create(String name, String email) {
    return new Identity()
                .setName(name)
                .setEmail(email);
  }
}
