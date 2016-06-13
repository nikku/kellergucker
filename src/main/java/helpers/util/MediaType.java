package helpers.util;

import spark.Request;

/**
 *
 * @author nikku
 */
public class MediaType {

  public static final String APPLICATION_JSON = "application/json";
  public static final String TEXT_HTML = "text/html";


  public static boolean acceptsHtml(Request request) {
    return accepts(request, TEXT_HTML);
  }

  public static boolean acceptsJson(Request request) {
    return accepts(request, APPLICATION_JSON);
  }

  public static boolean accepts(Request request, String contentType) {
    String accept = request.headers("Accept");
    
    return accept != null && accept.contains(contentType);
  }
}
