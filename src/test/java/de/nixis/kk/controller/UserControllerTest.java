package de.nixis.kk.controller;

import com.google.api.client.http.HttpResponse;
import de.nixis.kk.data.user.CreateTrigger;
import de.nixis.kk.data.user.CreateUser;
import de.nixis.kk.helpers.JsonContent;
import de.nixis.kk.helpers.env.ServerEnvironment;
import org.junit.ClassRule;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author nikku
 */
public class UserControllerTest {

  @ClassRule
  public static ServerEnvironment env = new ServerEnvironment() {{
    executeMigrations = true;
  }};


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


    // when
    HttpResponse response =
            env.client().request("POST /users")
              .setContent(JsonContent.create(details))
              .execute();

    // then
    assertThat(response.getStatusCode()).isEqualTo(200);

    String expectedBody =
      "{\"name\":\"FOO\",\"email\":\"foo@bar\",\"created\":00000,\"stocks\":[" +
        "{\"name\":\"BAR\",\"symbol\":\"CAA1\",\"url\":\"https://finance.yahoo.com/q?s=CAA1\",\"triggers\":{\"buy\":100.0,\"sell\":300.0},\"quotes\":{\"open\":-1.0,\"high\":-1.0,\"low\":-1.0,\"close\":-1.0,\"volume\":-1.0,\"adjustedClose\":-1.0}}," +
        "{\"name\":\"FOO\",\"symbol\":\"CXX1\",\"url\":\"https://finance.yahoo.com/q?s=CXX1\",\"triggers\":{\"buy\":100.0,\"sell\":300.0},\"quotes\":{\"open\":-1.0,\"high\":-1.0,\"low\":-1.0,\"close\":-1.0,\"volume\":-1.0,\"adjustedClose\":-1.0}}" +
      "]}";

    String responseBody = response.parseAsString().replaceFirst("\"created\":[^,]+", "\"created\":00000");

    assertThat(responseBody).isEqualTo(expectedBody);
  }

}
