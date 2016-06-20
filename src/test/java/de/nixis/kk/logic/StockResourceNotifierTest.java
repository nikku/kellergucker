package de.nixis.kk.logic;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import de.nixis.kk.data.notifications.Identity;
import de.nixis.kk.data.notifications.Mail;
import de.nixis.kk.data.user.CreateTrigger;
import de.nixis.kk.data.user.CreateUser;
import de.nixis.kk.helpers.env.DatabaseEnvironment;
import de.nixis.kk.helpers.mock.MockMailer;
import de.nixis.kk.helpers.template.Templates;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class StockResourceNotifierTest {

  @Rule
  public DatabaseEnvironment env = new DatabaseEnvironment() {{
    executeMigrations = true;
  }};

  private UserResource userResource;
  private StockResource stockResource;

  private MockMailer mailer;
  private EmailNotifier emailNotifier;

  private Templates templates;

  @Before
  public void before() {

    mailer = new MockMailer();
    templates = new Templates();
    emailNotifier = new EmailNotifier(templates, mailer);

    userResource = new UserResource(env.db());
    stockResource = new StockResource(env.db(), emailNotifier);
  }

  @Test
  public void shouldFetch() {

    // given
    createStocks();

    LocalDate date = LocalDate.of(2015, 10, 21);

    // when
    stockResource.updateQuotes(date);

    stockResource.sendRecommendations(date.minusDays(1));

    // then
    List<Mail> mails = mailer.getSentMails();

    assertThat(mails).hasSize(1);

    Mail mail = mails.get(0);

    String body = mail.getBody();

    assertThat(mail.getReceiver()).isEqualTo(Identity.create("FOO", "foo@bar"));
    assertThat(mail.getSubject()).isEqualTo("[kellegucker] 2 investment recommendation(s)");

    assertThat(body).contains(
      "* ISHS-CO.MSCI EM.MAR.IMI UC.ETF",
      "SELL above 16 (current 19.02).",
      "https://finance.yahoo.com/q?s=IS3N.DE",
      "* DB X-TR.EO STOXX 50 ETF DR 1C" +
      "BUY below 43 (current 42.37)." +
      "https://finance.yahoo.com/q?s=DXET.DE"
    );
  }


  protected void createStocks() {
    // given
    CreateTrigger trigger1 =
        new CreateTrigger()
            .setName("DB X-TR.EO STOXX 50 ETF DR 1C")
            .setSymbol("DXET.DE")
            .setBuy(43)
            .setSell(-1);

    CreateTrigger trigger2 =
        new CreateTrigger()
            .setName("ISHS-CO.MSCI EM.MAR.IMI UC.ETF")
            .setSymbol("IS3N.DE")
            .setBuy(-1)
            .setSell(16);

    CreateUser details =
        new CreateUser()
          .setEmail("foo@bar")
          .setName("FOO")
          .setTriggers(asList(trigger1, trigger2));

    userResource.createUser(details);
  }

}
