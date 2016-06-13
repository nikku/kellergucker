package de.nixis.kk.data.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class User {

  private String name;
  private String email;
  private Date created;

}
