package de.nixis.kk.helpers.util;

import java.util.UUID;

/**
 * Created by nikku on 6/12/16.
 */
public class Ids {

  public static String generate() {
    return UUID.randomUUID().toString();
  }
}
