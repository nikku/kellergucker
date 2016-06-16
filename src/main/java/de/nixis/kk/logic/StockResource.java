package de.nixis.kk.logic;

import java.time.LocalDate;
import java.util.List;

import de.nixis.kk.data.stocks.Quote;
import de.nixis.kk.data.stocks.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static java.time.LocalDate.now;

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

  public List<Stock> listStocks() {
    
    String stocksSql =
      "SELECT " +
        "DISTINCT t.stock_symbol as \"symbol\", " +
        "COALESCE(s.open, -1) as \"quotes.open\", " +
        "COALESCE(s.high, -1) as \"quotes.high\", " +
        "COALESCE(s.low, -1) as \"quotes.low\", " +
        "COALESCE(s.close, -1) as \"quotes.close\", " +
        "s.date as \"quotes.date\" " +
      "FROM user_triggers t " +
        "LEFT JOIN stocks s ON (t.stock_symbol = s.symbol)";

    try (Connection c = db.open()) {
      return c.createQuery(stocksSql).executeAndFetch(Stock.class);
    }
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(StockResource.class);


  public void updateQuotes() {
    updateQuotes(now());
  }

  public void updateQuotes(LocalDate to) {

    QuoteFetcher quoteFetcher = new QuoteFetcher();

    List<String> symbols = listSymbols();

    try {
      symbols.forEach((symbol) -> {

        LOGGER.info("Update quotes for <" + symbol + ">");

        LocalDate from = to.minusDays(7);

        List<Quote> fetchedQuotes = quoteFetcher.fetchQuotes(symbol, from, to);

        updateQuotes(symbol, fetchedQuotes);
      });
    } catch (Exception e) {
      LOGGER.error("Failed to update quotes", e);
    }
  }

  public void updateQuotes(String stockSymbol, List<Quote> quotes) {

    String insertStockSql =
            "INSERT INTO stocks (" +
              "symbol, date, open, high, low, close, volume, adjusted_close" +
            ") VALUES (" +
              ":symbol, :date, :open, :high, :low, :close, :volume, :adjustedClose" +
            ") ON CONFLICT (symbol) DO UPDATE SET " +
              "open = EXCLUDED.open, " +
              "high = EXCLUDED.high, " +
              "low = EXCLUDED.low, " +
              "close = EXCLUDED.close, " +
              "volume = EXCLUDED.volume, " +
              "adjusted_close = EXCLUDED.adjusted_close";

    String insertHistoricStockSql =
            "INSERT INTO historic_stocks (" +
              "symbol, date, open, high, low, close, volume, adjusted_close" +
            ") VALUES (" +
              ":symbol, :date, :open, :high, :low, :close, :volume, :adjustedClose" +
            ") ON CONFLICT (symbol, date) DO NOTHING";

    try (Connection c = db.open()) {

      for (Quote quote: quotes) {
        c.createQuery(insertHistoricStockSql).bind(quote).addParameter("symbol", stockSymbol).executeUpdate();
      }

      Quote lastQuote = quotes.get(quotes.size() - 1);

      c.createQuery(insertStockSql).bind(lastQuote).addParameter("symbol", stockSymbol).executeUpdate();
    }

  }

  public List<Stock> listHistoricalStocks(String symbol) {

    String selectHistoricStockSql =
      "SELECT " +
        "s.symbol, " +
        "COALESCE(s.open, -1) as \"quotes.open\", " +
        "COALESCE(s.high, -1) as \"quotes.high\", " +
        "COALESCE(s.low, -1) as \"quotes.low\", " +
        "COALESCE(s.close, -1) as \"quotes.close\", " +
        "COALESCE(s.volume, -1) as \"quotes.volume\", " +
        "COALESCE(s.adjusted_close, -1) as \"quotes.adjustedClose\", " +
        "s.date as \"quotes.date\" " +
      "FROM historic_stocks s " +
      "WHERE s.symbol = :symbol";

    try (Connection c = db.open()) {
      return c.createQuery(selectHistoricStockSql).addParameter("symbol", symbol).executeAndFetch(Stock.class);
    }
  }
}
