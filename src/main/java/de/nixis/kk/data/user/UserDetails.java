package de.nixis.kk.data.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class UserDetails {

  private String name;
  private String email;
  private Date created;

  private List<Stock> stocks = new ArrayList<>();
}
