package de.nixis.kk.controller;

import java.util.List;

import de.nixis.kk.data.ServerOptions;
import de.nixis.kk.data.stocks.Stock;
import de.nixis.kk.logic.StockResource;
import helpers.controller.AbstractController;
import helpers.template.Templates;
import spark.Request;
import spark.Response;

import static helpers.util.Asserts.ensureAdmin;
import static helpers.util.MediaType.APPLICATION_JSON;

/**
 *
 * @author nikku
 */
public class StockController extends AbstractController {

  private final StockResource stockResource;
  private final ServerOptions options;


  public StockController(Templates templates, StockResource stockResource, ServerOptions options) {
    super(templates);

    this.stockResource = stockResource;
    this.options = options;
  }

  
  public Object list(Request request, Response response) {

    ensureAdmin(request, options.getAdminKey());

    List<Stock> userDetails = stockResource.listStocks();

    // we respond as json
    response.type(APPLICATION_JSON);

    return toJson(userDetails);
  }

}
