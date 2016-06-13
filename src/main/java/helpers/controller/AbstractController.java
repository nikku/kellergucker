package helpers.controller;

import helpers.template.Templates;
import helpers.util.Json;

/**
 *
 * @author nikku
 */
public class AbstractController {

  protected final Templates templates;

  public AbstractController(Templates templates) {
    this.templates = templates;
  }

  protected String toJson(Object o) {
    return Json.stringify(o);
  }

  protected <T> T parseJson(Class<T> cls, String str) {
    return Json.parse(cls, str);
  }
}
