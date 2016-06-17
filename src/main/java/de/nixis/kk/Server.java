package de.nixis.kk;

import de.nixis.kk.controller.HealthController;
import de.nixis.kk.controller.RootController;
import de.nixis.kk.controller.StockController;
import de.nixis.kk.controller.UserController;
import de.nixis.kk.data.MailerOptions;
import de.nixis.kk.data.ServerOptions;
import de.nixis.kk.helpers.template.Templates;
import de.nixis.kk.logic.EmailNotifier;
import de.nixis.kk.logic.StockResource;
import de.nixis.kk.logic.UserResource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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


  protected final ServerOptions options;

  protected Service http;
  protected Sql2o db;
  protected Templates templates;

  protected ScheduledExecutorService executor;

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

    // mailer
    MailerOptions mailerOptions = options.getMailerOptions();

    EmailNotifier emailNotifier = null;

    if (mailerOptions != null) {
      emailNotifier = new EmailNotifier(templates, mailerOptions);
    }

    // logic
    UserResource userResource = new UserResource(db);
    StockResource stockResource = new StockResource(db, emailNotifier);

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
      LOGGER.info("Update quotes");

      stockResource.updateQuotes();
    }, 3 * 1000, ONE_DAY_MS, TimeUnit.MILLISECONDS);

    // ONE_MINUTE_MS,
    
    executor.scheduleWithFixedDelay(() -> {
      LOGGER.info("Send recommendations");

      stockResource.sendRecommendations();
    }, 10 * 1000, ONE_DAY_MS, TimeUnit.MILLISECONDS);

    // ONE_MINUTE_MS * 5
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
