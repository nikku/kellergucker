package de.nixis.kk.data.stocks;

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

  private Triggers triggers;

  private Quotes quotes;

}
