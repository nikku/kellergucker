package de.nixis.kk.helpers;

import java.io.UnsupportedEncodingException;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpContent;
import de.nixis.kk.helpers.util.Json;

/**
 *
 * @author nikku
 */
public class JsonContent {

  public static HttpContent create(Object o) throws UnsupportedEncodingException {
    String body = Json.stringify(o);

    byte[] bytes = body.getBytes("UTF-8");

    return new ByteArrayContent("application/json", bytes);
  }
}
