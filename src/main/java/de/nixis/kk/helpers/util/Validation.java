package de.nixis.kk.helpers.util;

import java.util.Collection;

/**
 * Created by nikku on 6/12/16.
 */
public class Validation {

  public static boolean allValid(Collection<? extends Validatable> elements) {
    return elements != null && elements.stream().allMatch((e) -> e.isValid());
  }

}
