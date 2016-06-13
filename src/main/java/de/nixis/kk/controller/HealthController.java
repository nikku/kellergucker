package de.nixis.kk.controller;

import de.nixis.kk.data.Health;
import de.nixis.kk.data.user.User;
import helpers.template.Templates;
import helpers.controller.AbstractController;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Request;
import spark.Response;


/**
 *
 * @author nikku
 */
public class HealthController extends AbstractController {

  private final Sql2o db;

  public HealthController(Templates templates, Sql2o db) {
    super(templates);

    this.db = db;
  }

  public String check(Request request, Response response) {
    try (Connection c = db.open()) {
      c.createQuery("SELECT name FROM users")
          .executeAndFetchFirst(User.class);
    }
    
    return toJson(new Health());
  }
}
