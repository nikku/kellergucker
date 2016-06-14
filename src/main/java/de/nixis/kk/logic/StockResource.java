package de.nixis.kk.logic;

import java.util.List;

import de.nixis.kk.data.stocks.Stock;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 *
 * @author nikku
 */
public class StockResource {

  private final Sql2o db;

  public StockResource(Sql2o db) {
    this.db = db;
  }

  public List<String> listSymbols() {

    String stocksSql = "SELECT DISTINCT stock_symbol FROM user_triggers";

    try (Connection c = db.open()) {
      return c.createQuery(stocksSql).executeScalarList(String.class);
    }
  }

  public List<Stock> list() {
    
    String stocksSql =
      "SELECT " +
        "DISTINCT t.stock_symbol as \"symbol\", " +
        "COALESCE(s.open, -1) as \"quotes.open\", " +
        "COALESCE(s.close, -1) as \"quotes.close\", " +
        "COALESCE(s.high, -1) as \"quotes.high\", " +
        "COALESCE(s.low, -1) as \"quotes.low\", " +
        "s.updated as \"quotes.updated\" " +
      "FROM user_triggers t " +
        "LEFT JOIN stocks s ON (t.stock_symbol = s.symbol)";

    try (Connection c = db.open()) {
      return c.createQuery(stocksSql).executeAndFetch(Stock.class);
    }
  }

}
