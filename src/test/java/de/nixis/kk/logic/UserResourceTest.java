package de.nixis.kk.logic;

import de.nixis.kk.data.user.CreateTrigger;
import de.nixis.kk.data.user.CreateUser;
import de.nixis.kk.data.user.UserDetails;
import helpers.env.DatabaseEnvironment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nikku on 6/12/16.
 */
public class UserResourceTest {

  @Rule
  public DatabaseEnvironment env = new DatabaseEnvironment() {{
    executeMigrations = true;
  }};


  private UserResource userResource;

  @Before
  public void before() {
    userResource = new UserResource(env.db());
  }


  @Test
  public void shouldManage() {

    // given
    CreateTrigger trigger1 =
        new CreateTrigger()
            .setName("FOO")
            .setSymbol("CXX1")
            .setBuy(100)
            .setSell(300);

    CreateTrigger trigger2 =
        new CreateTrigger()
            .setName("BAR")
            .setSymbol("CAA1")
            .setBuy(100)
            .setSell(300);

    CreateUser details =
        new CreateUser()
          .setEmail("foo@bar")
          .setName("FOO")
          .setTriggers(asList(trigger1, trigger2));

    // when
    String id = userResource.createUser(details);

    // then
    UserDetails userDetails = userResource.getDetails(id);

    assertThat(userDetails.getName()).isEqualTo("FOO");
    assertThat(userDetails.getEmail()).isEqualTo("foo@bar");

    String stocksAsString = userDetails.getStocks().toString();

    String expectedString =
        "[" +
          "Stock(" +
            "name=BAR, " +
            "symbol=CAA1, " +
            "url=https://finance.yahoo.com/q?s=CAA1, " +
            "triggers=Triggers(buy=100.0, sell=300.0), " +
            "quotes=Quote(open=-1.0, high=-1.0, low=-1.0, close=-1.0, volume=-1.0, adjustedClose=-1.0, date=null)" +
          "), " +
          "Stock(" +
            "name=FOO, " +
            "symbol=CXX1, " +
            "url=https://finance.yahoo.com/q?s=CXX1, " +
            "triggers=Triggers(buy=100.0, sell=300.0), " +
            "quotes=Quote(open=-1.0, high=-1.0, low=-1.0, close=-1.0, volume=-1.0, adjustedClose=-1.0, date=null)" +
          ")" +
        "]";

    assertThat(stocksAsString).isEqualTo(expectedString);

    // but when
    userResource.removeUser(id);

    // then
    UserDetails removedDetails = userResource.getDetails(id);

    assertThat(removedDetails).isNull();
  }
}
