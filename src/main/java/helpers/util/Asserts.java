package helpers.util;

import helpers.BadRequestException;
import spark.Request;

/**
 *
 * @author nikku
 */
public class Asserts {

  public static void ensureValid(Validatable v) {

    if (!v.isValid()) {
      throw new BadRequestException("Invalid input", 400);
    }
  }

  public static void ensureAdmin(Request request, String adminKey) {

    String t = request.queryParams("t");

    if (adminKey != null && !adminKey.equals(t)) {
      throw new BadRequestException("forbidden :-(", 401);
    }
  }
  
}
