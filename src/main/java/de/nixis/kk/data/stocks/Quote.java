package de.nixis.kk.data.stocks;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class Quote {

  private double open = -1;
  private double high = -1;
  private double low = -1;
  private double close = -1;
  private double volume = -1;
  private double adjustedClose = -1;

  private String date;
}
