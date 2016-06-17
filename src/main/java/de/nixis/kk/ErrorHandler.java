package de.nixis.kk;

import de.nixis.kk.data.Status;
import de.nixis.kk.helpers.ApplicationException;
import de.nixis.kk.helpers.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ExceptionHandler;
import spark.HaltException;
import spark.Request;
import spark.Response;

import static de.nixis.kk.helpers.util.MediaType.APPLICATION_JSON;
import static de.nixis.kk.helpers.util.MediaType.acceptsJson;

/**
 *
 * @author nikku
 */
public class ErrorHandler implements ExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

  @Override
  public void handle(Exception e, Request request, Response response) {

    // rethrow for halt(...) to work
    if (e instanceof HaltException) {
      throw (HaltException) e;
    }

    ApplicationException exception = ApplicationException.wrap("Ooops, this was an error.", e);

    // log severe errors
    if (exception.isServerError()) {
      LOGGER.error("Unhandled exception", exception);
    }

    response.status(exception.getCode());

    // TODO(nikku): application exception messages do not leak
    // any secrets and are save to transmit to the client (?)
    if (acceptsJson(request)) {
      response.type(APPLICATION_JSON);

      response.body(Json.stringify(Status.error(exception.getMessage())));
    } else {
      response.body(
        "<h1>HTTP " + exception.getCode() + "</h1>" +
        "<p>" + exception.getMessage() + "</p>"
      );
    }
  }

}
