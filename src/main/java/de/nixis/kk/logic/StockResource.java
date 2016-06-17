package de.nixis.kk.logic;

import static java.time.LocalDate.now;

import de.nixis.kk.data.stocks.Quote;
import de.nixis.kk.data.stocks.Recommendation;
import de.nixis.kk.data.stocks.Stock;
import de.nixis.kk.data.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 *
 * @author nikku
 */
public class StockResource {

  private final Sql2o db;
  private final EmailNotifier emailNotifier;

  public StockResource(Sql2o db, EmailNotifier emailNotifier) {
    this.db = db;
    this.emailNotifier = emailNotifier;
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

  public List<Recommendation> getChangeRecommendations() {
    String selectRecommendationSql =
        "SELECT " +
          "t.stock_symbol as \"stock.symbol\", " +
          "t.name as \"stock.name\", " +
          "t.buy as \"stock.triggers.buy\", " +
          "t.sell as \"stock.triggers.sell\", " +
          "s.open as \"stock.quotes.open\", " +
          "s.close as \"stock.quotes.close\", " +
          "s.high as \"stock.quotes.high\", " +
          "s.low as \"stock.quotes.low\", " +
          "s.date as \"stock.quotes.date\", " +
          "(CASE " +
            "WHEN t.buy <> -1 AND s.high < t.buy THEN 'BUY' " +
            "WHEN t.sell <> -1 AND s.low > t.sell THEN 'SELL' " +
            "ELSE 'HOLD' " +
          "END) as type, " +
          "u.id as \"user.id\", " +
          "u.name as \"user.name\", " +
          "u.email as \"user.email\" " +
        "FROM user_triggers t " +
          "JOIN stocks s ON (t.stock_symbol = s.symbol) " +
          "JOIN users u ON (t.user_id = u.id) " +
        "WHERE (CASE " +
            "WHEN t.buy <> -1 AND s.high < t.buy THEN 'BUY' " +
            "WHEN t.sell <> -1 AND s.low > t.sell THEN 'SELL' " +
            "ELSE 'HOLD' " +
          "END) <> 'HOLD'";

    try (Connection c = db.open()) {
      return c.createQuery(selectRecommendationSql).executeAndFetch(Recommendation.class);
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

        LOGGER.info("Fetch <" + symbol + ">");

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

    if (quotes.isEmpty()) {
      return;
    }

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

  public void sendRecommendations() {

    if (emailNotifier == null) {
      LOGGER.info("Skipping recommendations: No notifier configured");

      return;
    }


    List<Recommendation> recommendations = getChangeRecommendations();

    LOGGER.info("{} recommendations", recommendations.size());

    Map<User, List<Recommendation>> groupedRecommendations = recommendations.stream().collect(Collectors.groupingBy((r) -> {
      return r.getUser();
    }));

    groupedRecommendations.forEach((user, userRecommendations) -> {
      emailNotifier.sendNotificationEmail(user, userRecommendations);
    });
  }
}
