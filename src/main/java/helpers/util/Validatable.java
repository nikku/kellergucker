package helpers.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by nikku on 6/12/16.
 */
public interface Validatable {

  @JsonIgnore
  public boolean isValid();
}
