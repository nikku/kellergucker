package helpers;

/**
 * Exception thrown to indicate errors inside the application.
 * 
 * @author nikku
 */
public class ApplicationException extends RuntimeException {

  private final int code;

  public ApplicationException(String message, Throwable cause, int code) {
    super(message, cause);

    this.code = code;
  }

  public ApplicationException(String message, Throwable cause) {
    this(message, cause, 500);
  }

  public ApplicationException(String message, int code) {
    this(message, null, code);
  }

  public int getCode() {
    return code;
  }
}
