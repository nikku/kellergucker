package de.nixis.kk.logic;

import static org.assertj.core.api.Assertions.assertThat;

import de.nixis.kk.data.stocks.Quote;
import java.time.LocalDate;
import java.util.List;
import org.junit.Test;


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

    Quote expectedFirst =
            new Quote()
              .setOpen(174.5)
              .setHigh(174.83)
              .setLow(172.9)
              .setClose(174.75)
              .setVolume(4900.0)
              .setAdjustedClose(163.4327)
              .setDate("2013-10-01");

    Quote expectedLast =
            new Quote()
              .setOpen(186.51)
              .setHigh(191.03)
              .setLow(186.51)
              .setClose(188.55)
              .setVolume(8600.0)
              .setAdjustedClose(176.339)
              .setDate("2013-11-01");

    // then
    assertThat(quotes).hasSize(23);

    assertThat(quotes.get(0)).isEqualTo(expectedFirst);
    assertThat(quotes.get(quotes.size() - 1)).isEqualTo(expectedLast);
  }

}
