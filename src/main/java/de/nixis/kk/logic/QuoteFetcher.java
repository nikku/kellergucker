package de.nixis.kk.logic;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import de.nixis.kk.data.stocks.Quote;
import helpers.ApplicationException;

import static java.lang.Double.parseDouble;


/**
 *
 * @author nikku
 */
public class QuoteFetcher {

  private final Client client;

  public QuoteFetcher() {
    this.client = new Client();
  }
  
  /**
   * Fetch quotes for a given symbol for a given time range.
   * 
   * @param symbol
   * @param from
   * @param to
   * 
   * @return 
   */
  public List<Quote> fetchQuotes(String symbol, LocalDate from, LocalDate to) {

    String url = getQuotesUrl(symbol, from, to);

    try {
      String csv = client.query(url);

      return Stream.of(csv.split("\n")).skip(1).map((line) -> {

        String[] columns = line.split(",");

        Quote quotes =
            new Quote()
              .setDate(columns[0])
              .setOpen(parseDouble(columns[1]))
              .setHigh(parseDouble(columns[2]))
              .setLow(parseDouble(columns[3]))
              .setClose(parseDouble(columns[4]))
              .setVolume(parseDouble(columns[5]))
              .setAdjustedClose(parseDouble(columns[6]));

        return quotes;
      }).collect(Collectors.toList());
    } catch (IOException e) {
      throw new ApplicationException("query quotes failed", e);
    }
  }

  private static final String QUOTES_URL_PATTERN =
          "http://real-chart.finance.yahoo.com/table.csv?s=%s&a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=d&ignore=.csv";
  
  protected String getQuotesUrl(String symbol, LocalDate from, LocalDate to) {

    return String.format(
              QUOTES_URL_PATTERN,
              symbol,
              from.getMonthValue() - 1,
              from.getDayOfMonth(),
              from.getYear(),
              to.getMonthValue() - 1,
              to.getDayOfMonth(),
              to.getYear());
  }


  public static class Client {

    private final NetHttpTransport transport;
    private final HttpRequestFactory requestFactory;

    public Client() {
      this.transport = new NetHttpTransport();
      this.requestFactory = transport.createRequestFactory();
    }

    public String query(String url) throws IOException {
      HttpResponse response = request(url).execute();

      String body = response.parseAsString();

      response.disconnect();

      return body;
    }

    public HttpRequest request(String url) throws IOException {
      return requestFactory.buildRequest("GET", new GenericUrl(url), null);
    }
  
  }

}
