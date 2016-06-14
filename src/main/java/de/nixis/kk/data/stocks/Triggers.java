package de.nixis.kk.data.stocks;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author nikku
 */
@Data
@Accessors
public class Triggers {

  private double buy = -1;
  private double sell = -1;

}
