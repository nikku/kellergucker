package de.nixis.kk.controller;

import java.util.HashMap;

import de.nixis.kk.helpers.template.Templates;
import de.nixis.kk.helpers.controller.AbstractController;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import static de.nixis.kk.helpers.util.MediaType.TEXT_HTML;

/**
 *
 * @author nikku
 */
public class RootController extends AbstractController {

  public RootController(Templates templates) {
    super(templates);
  }

  public String index(Request request, Response response) {

    response.type(TEXT_HTML);

    HashMap<String, Object> foo = new HashMap<>();
    foo.put("bar", "BAR");

    return templates.render(new ModelAndView(foo, "hello.ftl"));
  }
}
