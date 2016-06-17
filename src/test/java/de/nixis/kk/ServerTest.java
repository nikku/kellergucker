package com.camunda.wm;

import static de.nixis.kk.helpers.assertions.Assertions.assertThat;

import com.google.api.client.http.HttpResponse;
import de.nixis.kk.helpers.env.ServerEnvironment;
import org.junit.Rule;
import org.junit.Test;


public class ServerTest {

  @Rule
  public ServerEnvironment env = new ServerEnvironment() {{
    startServer = false;
    executeMigrations = true;
  }};

  @Test
  public void shouldStart() throws Exception {

    // when
    env.server().run();

    // then
    HttpResponse response = env.client().query("GET /");

    assertThat(response).hasStatus(200);
  }

}
