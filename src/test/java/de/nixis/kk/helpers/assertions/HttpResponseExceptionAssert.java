package de.nixis.kk.helpers.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.api.client.http.HttpResponseException;
import de.nixis.kk.helpers.util.Json;
import java.io.IOException;
import org.assertj.core.api.AbstractThrowableAssert;


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

  public HttpResponseExceptionAssert containsPayload(String body) throws IOException {

    String actualPayload = actual.getContent();

    assertThat(actualPayload).describedAs("has payload").contains(body);

    return myself;
  }

  public HttpResponseExceptionAssert hasLocation(String expectedLocation) throws IOException {
    String location = actual.getHeaders().getLocation();

    assertThat(location).describedAs("has location header").isEqualTo(expectedLocation);

    return myself;
  }

  public HttpResponseExceptionAssert hasContentType(String expectedContentType) {
    String contentType = actual.getHeaders().getContentType();

    assertThat(contentType).describedAs("has content type").isEqualTo(expectedContentType);

    return myself;
  }

}
