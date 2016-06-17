package de.nixis.kk.helpers.controller;

import de.nixis.kk.helpers.template.Templates;
import de.nixis.kk.helpers.util.Json;

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
