package de.nixis.kk.data.user;

import lombok.Data;
import lombok.experimental.Accessors;
import helpers.util.Validatable;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class CreateTrigger implements Validatable {

  private String name;
  private String symbol;

  private double buy = -1;
  private double sell = -1;


  @Override
  public boolean isValid() {
    return
        name != null &&
        symbol != null;
  }
}
