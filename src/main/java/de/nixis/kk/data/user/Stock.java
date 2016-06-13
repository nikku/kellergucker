package de.nixis.kk.data.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class Stock {

  private String name;
  private String symbol;
  private String url;

  private Triggers triggers = new Triggers();

  public Quotes quotes = new Quotes();

  public Triggers getTriggers() {
    return triggers;
  }

  public void setTriggers(Triggers entity) {
    this.triggers = triggers;
  }

  @Data
  @Accessors
  public static class Triggers {
    private double buy = -1;
    private double sell = -1;
  }

}
