package de.nixis.kk.controller;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import de.nixis.kk.data.user.CreateTrigger;
import de.nixis.kk.data.user.CreateUser;
import helpers.AbstractServerTest;
import helpers.JsonContent;
import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 *
 * @author nikku
 */
public class UserControllerTest extends AbstractServerTest {

  @BeforeClass
  public static void setupSchema() {
    migrations.migrate();
  }

  
  @Test
  public void shouldAddUser() throws Exception {

    // given
    CreateTrigger trigger1 =
        new CreateTrigger()
            .setName("FOO")
            .setSymbol("CXX1")
            .setBuy(100)
            .setSell(300);

    CreateTrigger trigger2 =
        new CreateTrigger()
            .setName("BAR")
            .setSymbol("CAA1")
            .setBuy(100)
            .setSell(300);

    CreateUser details =
        new CreateUser()
          .setEmail("foo@bar")
          .setName("FOO")
          .setTriggers(asList(trigger1, trigger2));

    String location = null;

    try {
      // when
      client
          .request("POST /users")
              .setContent(JsonContent.create(details))
              .setFollowRedirects(false)
              .execute();
    
      fail("expected redirect");

    } catch (HttpResponseException e) {

      HttpHeaders headers = e.getHeaders();

      location = headers.getFirstHeaderStringValue("location");

      // then
      assertThat(e.getStatusCode()).isEqualTo(302);
      
      assertThat(location).matches(".*/users/.*");
    }

    // but when
    HttpResponse response = client.request("GET " + location).execute();

    // then
    assertThat(response.getStatusCode()).isEqualTo(200);

    String expectedBody =
      "{\"name\":\"FOO\",\"email\":\"foo@bar\",\"created\":00000,\"stocks\":[" +
        "{\"name\":\"BAR\",\"symbol\":\"CAA1\",\"url\":\"https://finance.yahoo.com/q?s=CAA1\",\"triggers\":{\"buy\":100.0,\"sell\":300.0},\"quotes\":{\"open\":-1.0,\"close\":-1.0,\"high\":-1.0,\"low\":-1.0,\"volume\":-1.0,\"adjustedClose\":-1.0}}," +
        "{\"name\":\"FOO\",\"symbol\":\"CXX1\",\"url\":\"https://finance.yahoo.com/q?s=CXX1\",\"triggers\":{\"buy\":100.0,\"sell\":300.0},\"quotes\":{\"open\":-1.0,\"close\":-1.0,\"high\":-1.0,\"low\":-1.0,\"volume\":-1.0,\"adjustedClose\":-1.0}}" +
      "]}";

    String responseBody = response.parseAsString().replaceFirst("\"created\":[^,]+", "\"created\":00000");

    assertThat(responseBody).isEqualTo(expectedBody);
  }

}
