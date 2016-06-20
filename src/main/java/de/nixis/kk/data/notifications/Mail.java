package de.nixis.kk.data.notifications;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Mail {

  private Identity sender;
  private Identity receiver;

  private String subject;
  private String body;

}
