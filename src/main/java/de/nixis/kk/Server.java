package de.nixis.kk;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.nixis.kk.controller.HealthController;
import de.nixis.kk.controller.RootController;
import de.nixis.kk.controller.StockController;
import de.nixis.kk.controller.UserController;
import de.nixis.kk.data.ServerOptions;
import de.nixis.kk.logic.StockResource;
import de.nixis.kk.logic.UserResource;
import helpers.template.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;
import spark.Service;

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

    templates = new Templates().setCache(options.isCacheTemplates());

    http = Service.ignite().port(options.getPort());


    // logic
    UserResource userResource = new UserResource(db);
    StockResource stockResource = new StockResource(db);

    // controllers
    RootController rootController = new RootController(templates);
    HealthController healthController = new HealthController(templates, db);
    UserController userController = new UserController(templates, userResource, options);
    StockController stockController = new StockController(templates, stockResource, options);

    // routes
    http.get("/", rootController::index);

    http.get("/health", healthController::check);

    http.post("/users", userController::createUser);
    http.get("/users/:id", userController::getUser);
    http.delete("/users/:id", userController::removeUser);

    http.get("/stocks", stockController::list);

    // error handling
    http.exception(Exception.class, new ErrorHandler());


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
