package helpers;

/**
 * Created by nikku on 6/12/16.
 */
public class BadRequestException extends ApplicationException {

  public BadRequestException(String message, Throwable cause) {
    super(message, cause, 400);
  }

  public BadRequestException(String message, int code) {
    super(message, code);
  }

}