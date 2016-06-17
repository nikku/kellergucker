package de.nixis.kk.data;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class MailerOptions {

  private String username;
  private String password;
  private String host;
  private boolean useTLS;

}
