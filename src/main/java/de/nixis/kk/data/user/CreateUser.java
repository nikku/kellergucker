package de.nixis.kk.data.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helpers.util.Validatable;
import lombok.Data;
import lombok.experimental.Accessors;

import static helpers.util.Validation.allValid;

/**
 * Created by nikku on 6/12/16.
 */
@Data
@Accessors(chain = true)
public class CreateUser implements Validatable {

  private String name;
  private String email;
  private Date created;

  private List<CreateTrigger> triggers = new ArrayList<>();

  @Override
  public boolean isValid() {
    return
        name != null &&
        email != null &&
        allValid(triggers);
  }
}
