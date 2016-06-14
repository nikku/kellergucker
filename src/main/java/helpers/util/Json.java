package helpers.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nikku on 6/12/16.
 */
public class Json {

  private static final Logger LOGGER = LoggerFactory.getLogger(Json.class);

  private static final ObjectMapper MAPPER;
  
  static {
    MAPPER = new ObjectMapper();
    
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public static <T> T parse(Class<T> cls, String text) {
    try {
      return MAPPER.readValue(text, cls);
    } catch (IOException e) {
      
      LOGGER.warn("JSON parse error", e);

      throw new BadRequestException("failed to parse JSON as <" + cls.getName() + ">", e);
    }
  }

  public static String stringify(Object o) {
    try {
      return MAPPER.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("failed to serialize object <" + o + ">", e);
    }
  }
}
