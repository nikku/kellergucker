package helpers.assertions;

import java.io.IOException;

import com.google.api.client.http.HttpResponseException;
import helpers.util.Json;
import org.assertj.core.api.AbstractThrowableAssert;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author nikku
 */
public class HttpResponseExceptionAssert extends AbstractThrowableAssert<HttpResponseExceptionAssert, HttpResponseException> {

  public HttpResponseExceptionAssert(HttpResponseException actual) {
    super(actual, HttpResponseExceptionAssert.class);
  }

  public HttpResponseExceptionAssert hasStatus(int statusCode) {
    int actualStatusCode = actual.getStatusCode();

    assertThat(actualStatusCode).describedAs("has HTTP code").isEqualTo(statusCode);

    return myself;
  }

  public HttpResponseExceptionAssert hasPayload(Object payload) throws IOException {
    String expectedPayload = Json.stringify(payload);

    return hasPayload(expectedPayload);
  }

  public HttpResponseExceptionAssert hasPayload(String body) throws IOException {
    String actualPayload = actual.getContent();

    assertThat(actualPayload).describedAs("has payload").isEqualTo(body);

    return myself;
  }

  public HttpResponseExceptionAssert hasLocationHeader(String expectedLocation) throws IOException {
    String location = actual.getHeaders().getLocation();

    assertThat(location).describedAs("has location header").isEqualTo(expectedLocation);

    return myself;
  }
}
