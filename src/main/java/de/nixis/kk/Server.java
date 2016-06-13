package de.nixis.kk;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.nixis.kk.controller.HealthController;
import de.nixis.kk.controller.RootController;
import de.nixis.kk.controller.UserController;
import de.nixis.kk.data.ServerOptions;
import de.nixis.kk.data.Status;
import de.nixis.kk.logic.UserResource;
import helpers.ApplicationException;
import helpers.template.Templates;
import helpers.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;
import spark.Service;

import static helpers.util.MediaType.acceptsJson;

/**
 *
 * @author nikku
 */
public class Server {
  
  public static final int ONE_MINUTE_MS = 1000 * 60;
  public static final int ONE_DAY_MS = ONE_MINUTE_MS * 60 * 24;

  public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  
  private final ServerOptions options;

  private Service http;
  private Sql2o db;
  private Templates templates;

  private ScheduledExecutorService executor;

  public Server(ServerOptions options) {
    this.options = options;
  }


  /**
   * Run the application.
   */
  public void run() {

    // basic
    db = new Sql2o(options.getJdbcUrl(), null, null);

    templates = new Templates().setCache(options.isProduction());

    http = Service.ignite().port(options.getPort());


    // logic
    UserResource userResource = new UserResource(db);

    // controllers
    RootController rootController = new RootController(templates);
    HealthController healthController = new HealthController(templates, db);
    UserController userController = new UserController(templates, userResource, options);

    // routes
    http.get("/", rootController::index);

    http.get("/health", healthController::check);

    http.post("/users", userController::createUser);
    http.get("/users/:id", userController::getUser);
    http.delete("/users/:id", userController::removeUser);

    // error handling
    http.exception(Exception.class, (exception, request, response) -> {

      int errorCode = 500;
      
      if (exception instanceof ApplicationException) {
        errorCode = ((ApplicationException) exception).getCode();
      }

      // log severe errors
      if (errorCode >= 500) {
        LOGGER.error("Unhandled error", exception);
      }

      response.status(errorCode);

      if (acceptsJson(request)) {
        response.body(Json.stringify(Status.error()));
      } else {
        response.body(
          "<h1>HTTP " + errorCode + "</h1>" +
          "<p>Ooops, this was an error!</p>"
        );
      }
    });


    ///////// task scheduler /////////////////////////

    executor = Executors.newScheduledThreadPool(1);

    executor.scheduleWithFixedDelay(() -> {

      // do scheduled work

    }, ONE_MINUTE_MS, ONE_DAY_MS, TimeUnit.DAYS);

    
    http.awaitInitialization();

    LOGGER.info("Server running");
  }

  public void stop() {
    http.stop();
    executor.shutdown();
    templates.destroy();
    
    LOGGER.info("Server stopped");
  }

}
