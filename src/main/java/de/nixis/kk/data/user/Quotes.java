package de.nixis.kk.data.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class Quotes {

  private double open = -1;
  private double close = -1;
  private double high = -1;
  private double low = -1;
  private double volume = -1;
  private double adjustedClose = -1;

  private Date updated;
}
