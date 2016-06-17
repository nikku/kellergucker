package de.nixis.kk;

import static de.nixis.kk.helpers.assertions.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import de.nixis.kk.data.ServerOptions;
import de.nixis.kk.data.Status;
import de.nixis.kk.helpers.BadRequestException;
import de.nixis.kk.helpers.env.ServerEnvironment;
import org.junit.ClassRule;
import org.junit.Test;
import spark.Service;


public class ErrorHandlerTest {

  @ClassRule
  public static ServerEnvironment env = new TestServerEnvironment();


  @Test
  public void shouldHandleHalt() throws Exception {

    try {
      // when
      env.client().query("GET /halt-exception");

      fail("expected HTTP 404");
    } catch (HttpResponseException e) {
      // then
      assertThat(e)
          .hasStatus(404)
          .containsPayload("halt");
    }
  }

  @Test
  public void shouldHandleBadRequestException() throws Exception {

    try {
      // when
      env.client().query("GET /bad-request-exception");

      fail("expected HTTP 400");
    } catch (HttpResponseException e) {

      // then
      assertThat(e)
          .hasStatus(400)
          .containsPayload("bad request");
    }
  }

  @Test
  public void shouldHandleRuntimeException() throws Exception {

    try {
      // when
      env.client().query("GET /runtime-exception");

      fail("expected HTTP 500");
    } catch (HttpResponseException e) {

      // then
      assertThat(e)
          .hasStatus(500)
          .containsPayload("Ooops, this was an error.");
    }
  }

  @Test
  public void shouldRespondWithJSON() throws Exception {


    try {
      HttpHeaders headers = new HttpHeaders().setAccept("application/json");

      // when
      env.client().request("GET /runtime-exception")
                      .setHeaders(headers)
                      .execute();

      fail("expected HTTP 500");
    } catch (HttpResponseException e) {

      Status expectedPayload = Status.error("Ooops, this was an error.");

      // then
      assertThat(e)
          .hasStatus(500)
          .hasContentType("application/json")
          .hasPayload(expectedPayload);
    }
  }

  @Test
  public void shouldHandleNoException() throws Exception {

    // when
    HttpResponse response = env.client().query("GET /no-exception");

    // then
    assertThat(response)
        .hasStatus(200)
        .containsPayload("ok");
  }


  ///////// helpers ////////////////////////////////////////

  private static class TestServerEnvironment extends ServerEnvironment {

    @Override
    protected Server createServer(ServerOptions serverOptions) {
      return new Server(serverOptions) {
        @Override
        public void run() {

          http = Service.ignite().port(options.getPort());

          http.before("/halt-exception", (request, response) -> http.halt(404, "halt"));

          http.exception(Exception.class, new ErrorHandler());

          http.get("/runtime-exception", (request, response) -> {
            throw new RuntimeException("OMG; secret message");
          });

          http.get("/no-exception", (request, response) -> "ok");

          http.get("/bad-request-exception", (request, response) -> {
            throw new BadRequestException("bad request", 400);
          });

          http.awaitInitialization();
        }

        @Override
        public void stop() {
          http.stop();
        }

      };

    }

  }

}
