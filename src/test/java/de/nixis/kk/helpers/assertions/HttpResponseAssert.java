package de.nixis.kk.helpers.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.api.client.http.HttpResponse;
import de.nixis.kk.helpers.util.Json;
import java.io.IOException;
import org.assertj.core.api.AbstractAssert;


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

  public HttpResponseAssert containsPayload(String body) throws IOException {

    String actualPayload = actual.parseAsString();

    assertThat(actualPayload).describedAs("has payload").contains(body);

    return myself;
  }

}
