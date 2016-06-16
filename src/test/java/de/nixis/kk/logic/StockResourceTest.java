package de.nixis.kk.logic;

import java.time.LocalDate;
import java.util.List;

import de.nixis.kk.data.stocks.Stock;
import de.nixis.kk.data.user.CreateTrigger;
import de.nixis.kk.data.user.CreateUser;
import helpers.AbstractDbTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nikku on 6/12/16.
 */
public class StockResourceTest extends AbstractDbTest {

  private UserResource userResource;
  private StockResource stockResource;

  @BeforeClass
  public static void beforeClass() {
    migrations.clean().migrate();
  }

  @Before
  public void before() {
    userResource = new UserResource(db);
    stockResource = new StockResource(db);
  }


  @Test
  public void shouldManage() {

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

    String id = userResource.createUser(details);

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
}
