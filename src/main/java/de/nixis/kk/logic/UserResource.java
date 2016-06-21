package de.nixis.kk.logic;

import de.nixis.kk.data.stocks.Stock;
import de.nixis.kk.data.user.CreateUser;
import de.nixis.kk.data.user.UserDetails;
import de.nixis.kk.helpers.BadRequestException;
import de.nixis.kk.helpers.util.Ids;
import java.util.Date;
import java.util.List;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;


/**
 * @author nikku
 */
public class UserResource {

  private final Sql2o db;

  public UserResource(Sql2o db) {
    this.db = db;
  }

  /**
   * Creat user and return the new users id.
   *
   * @param createDetails
   * @return
   */
  public String createUser(CreateUser createDetails) {

    String id = Ids.generate();

    String createUserSql =
        "INSERT INTO users (id, name, email, created) VALUES (:id, :name, :email, :created);--";

    String createTriggerSql =
        "INSERT INTO user_triggers (" +
            "user_id, stock_symbol, name, buy, sell, created" +
            ") VALUES (" +
            ":user_id, :symbol, :name, :buy, :sell, :created" +
            ");--";

    try (Connection c = db.open()) {

      c.createQuery(createUserSql)
          .bind(createDetails)
          .addParameter("id", id)
          .addParameter("created", new Date())
          .executeUpdate();

      createDetails.getTriggers().forEach((t) -> {

        c.createQuery(createTriggerSql)
            .bind(t)
            .addParameter("user_id", id)
            .addParameter("created", new Date())
            .executeUpdate();
      });
    } catch (Sql2oException e) {

      if (isConstraintViolation(e)) {
        throw new BadRequestException("duplicate key (email?)", e);
      } else {
        throw e;
      }
    }

    return id;
  }

  /**
   * Get details for a given user.
   *
   * @param id
   * @return
   */
  public UserDetails getDetails(String id) {

    String detailsSql =
        "SELECT name, email, created FROM users u WHERE u.id = :id";

    String stocksSql =
        "SELECT " +
          "t.stock_symbol as \"symbol\", " +
          "t.name as \"name\", " +
          "t.buy as \"triggers.buy\", " +
          "t.sell as \"triggers.sell\", " +
          "COALESCE(s.open, -1) as \"quotes.open\", " +
          "COALESCE(s.close, -1) as \"quotes.close\", " +
          "COALESCE(s.high, -1) as \"quotes.high\", " +
          "COALESCE(s.low, -1) as \"quotes.low\", " +
          "s.date as \"quotes.date\" " +
        "FROM user_triggers t " +
          "LEFT JOIN stocks s ON (t.stock_symbol = s.symbol)" +
        "WHERE t.user_id = :id";

    UserDetails userDetails;

    try (Connection c = db.open()) {
      userDetails =
          c.createQuery(detailsSql)
              .addParameter("id", id)
              .executeAndFetchFirst(UserDetails.class);

      if (userDetails == null) {
        return null;
      }

      List<Stock> stocks = c.createQuery(stocksSql)
          .addParameter("id", id)
          .executeAndFetch(Stock.class);

      stocks.forEach((s) -> {
        s.setUrl("https://finance.yahoo.com/q?s=" + s.getSymbol());
      });

      userDetails.setStocks(stocks);
    }

    return userDetails;
  }

  /**
   * Remove user with given id.
   *
   * @param id
   */
  public void removeUser(String id) {

    String detailsSql =
        "DELETE FROM users u WHERE u.id = :id";

    try (Connection c = db.open()) {
      c.createQuery(detailsSql)
          .addParameter("id", id)
          .executeUpdate();
    }
  }

  private static boolean isConstraintViolation(Sql2oException e) {
    return e.getMessage().contains("duplicate key value");
  }
}
