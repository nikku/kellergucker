package de.nixis.kk.data.stocks;

import de.nixis.kk.data.user.User;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Recommendation {

  private Stock stock;

  private Type type;

  private User user;

  public static enum Type {
    BUY,
    SELL,
    HOLD
  }

}
