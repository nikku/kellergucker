package de.nixis.kk.logic;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import de.nixis.kk.data.stocks.Recommendation;
import de.nixis.kk.data.stocks.Stock;
import de.nixis.kk.data.user.CreateTrigger;
import de.nixis.kk.data.user.CreateUser;
import de.nixis.kk.helpers.env.DatabaseEnvironment;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class StockResourceTest {

  @Rule
  public DatabaseEnvironment env = new DatabaseEnvironment() {{
    executeMigrations = true;
  }};

  private UserResource userResource;
  private StockResource stockResource;

  @Before
  public void before() {
    userResource = new UserResource(env.db());
    stockResource = new StockResource(env.db(), null);
  }

  @Test
  public void shouldFetch() {

    // given
    createStocks();

    // when
    stockResource.updateQuotes(LocalDate.of(2014, 10, 24));

    List<Stock> stocks = stockResource.listStocks();
    List<Stock> historicalStocks = stockResource.listHistoricalStocks("IS3N.DE");

    // then
    // both stocks inside
    assertThat(stocks).hasSize(2);

    // fetching last seven days, only six entries...
    assertThat(historicalStocks).hasSize(6);
  }

  @Test
  public void shouldUpdate() {

    // given
    createStocks();

    stockResource.updateQuotes(LocalDate.of(2014, 10, 24));

    // when
    stockResource.updateQuotes(LocalDate.of(2014, 10, 28));

    List<Stock> stocks = stockResource.listStocks();
    List<Stock> historicalStocks = stockResource.listHistoricalStocks("IS3N.DE");

    // then
    // both stocks inside
    assertThat(stocks).hasSize(2);

    // 6 + 2 additional entries
    assertThat(historicalStocks).hasSize(8);
  }

  @Test
  public void shouldRecommend() {

    // given
    createStocks();

    LocalDate date = LocalDate.of(2014, 10, 24);

    stockResource.updateQuotes(LocalDate.of(2014, 10, 24));

    List<Recommendation> recommendations = stockResource.getChangeRecommendations(date.minusDays(1));

    System.out.println(recommendations);
  }

  protected void createStocks() {
    // given
    CreateTrigger trigger1 =
        new CreateTrigger()
            .setName("DB X-TR.EO STOXX 50 ETF DR 1C")
            .setSymbol("DXET.DE")
            .setBuy(100)
            .setSell(300);

    CreateTrigger trigger2 =
        new CreateTrigger()
            .setName("ISHS-CO.MSCI EM.MAR.IMI UC.ETF")
            .setSymbol("IS3N.DE")
            .setBuy(100)
            .setSell(300);

    CreateUser details =
        new CreateUser()
          .setEmail("foo@bar")
          .setName("FOO")
          .setTriggers(asList(trigger1, trigger2));

    userResource.createUser(details);
  }

}
