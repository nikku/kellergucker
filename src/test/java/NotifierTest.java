import de.nixis.kk.data.user.User;
import org.junit.Test;

/**
 * Created by nikku on 6/12/16.
 */
public class NotifierTest {

  @Test
  public void shouldSendMail() {

    new Notifier().sendMail(new User());
  }
}
