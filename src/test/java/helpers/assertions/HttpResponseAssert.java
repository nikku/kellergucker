package helpers.assertions;

import java.io.IOException;

import com.google.api.client.http.HttpResponse;
import helpers.util.Json;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author nikku
 */
public class HttpResponseAssert extends AbstractAssert<HttpResponseAssert, HttpResponse> {

  public HttpResponseAssert(HttpResponse actual) {
    super(actual, HttpResponseAssert.class);
  }

  public HttpResponseAssert isSuccess() {
    int actualStatusCode = actual.getStatusCode();

    assertThat(actualStatusCode).describedAs("has success HTTP code").isLessThan(400);

    return myself;
  }

  public HttpResponseAssert hasStatus(int statusCode) {
    int actualStatusCode = actual.getStatusCode();

    assertThat(actualStatusCode).describedAs("has HTTP code").isEqualTo(statusCode);

    return myself;
  }

  public HttpResponseAssert hasPayload(Object payload) throws IOException {
    String expectedPayload = Json.stringify(payload);

    return hasPayload(expectedPayload);
  }

  public HttpResponseAssert hasPayload(String body) throws IOException {
    String actualPayload = actual.parseAsString();

    assertThat(actualPayload).describedAs("has payload").isEqualTo(body);

    return myself;
  }
}
