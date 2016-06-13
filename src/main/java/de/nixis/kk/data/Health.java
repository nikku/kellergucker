package de.nixis.kk.data;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class Health {

  private String status = "up";

}
