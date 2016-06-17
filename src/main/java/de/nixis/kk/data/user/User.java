package de.nixis.kk.data.user;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class User {

  private String id;
  private String name;
  private String email;
  private Date created;

}
