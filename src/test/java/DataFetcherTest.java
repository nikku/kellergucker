import java.time.LocalDate;
import java.util.List;

import de.nixis.kk.data.stocks.Quote;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nikku on 6/12/16.
 */
public class DataFetcherTest {

  @Test
  public void shouldFetchPrice() {

    // given
    String symbol = "VOW3.DE";
    LocalDate from = LocalDate.of(2013, 01, 01);
    LocalDate to = LocalDate.of(2013, 03, 01);

    // when
    List<Quote> prices = new DataFetcher().fetchQuotes(symbol, from, to);

    // then
    assertThat(prices).hasSize(42);
  }
}
