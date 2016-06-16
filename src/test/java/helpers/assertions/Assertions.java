package helpers.assertions;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

/**
 *
 * @author nikku
 */
public class Assertions {

  public static HttpResponseAssert assertThat(HttpResponse response) {
    return new HttpResponseAssert(response);
  }

  public static HttpResponseExceptionAssert assertThat(HttpResponseException exception) {
    return new HttpResponseExceptionAssert(exception);
  }

}