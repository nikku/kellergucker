package de.nixis.kk.logic;

import java.time.LocalDate;
import java.util.List;

import de.nixis.kk.data.stocks.Quote;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author nikku
 */
public class QuoteFetcherTest {

  @Test
  public void shouldFetch() throws Exception {

    // given
    QuoteFetcher quoteFetcher = new QuoteFetcher();

    // when
    List<Quote> quotes =
        quoteFetcher.fetchQuotes(
          "VOW3.F",
          LocalDate.of(2013, 10, 1),
          LocalDate.of(2013, 11, 1)
        );

    // then
    assertThat(quotes).hasSize(23);
  }

}
