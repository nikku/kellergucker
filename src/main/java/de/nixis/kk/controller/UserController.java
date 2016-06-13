package de.nixis.kk.controller;

import de.nixis.kk.data.ServerOptions;
import de.nixis.kk.data.Status;
import de.nixis.kk.data.user.CreateUser;
import de.nixis.kk.data.user.UserDetails;
import de.nixis.kk.logic.UserResource;
import helpers.controller.AbstractController;
import helpers.template.Templates;
import spark.Request;
import spark.Response;

import static helpers.util.Asserts.ensureAdmin;
import static helpers.util.Asserts.ensureValid;
import static helpers.util.MediaType.APPLICATION_JSON;

/**
 *
 * @author nikku
 */
public class UserController extends AbstractController {

  private final UserResource userResource;
  
  private final ServerOptions options;

  
  public UserController(Templates templates, UserResource userResource, ServerOptions options) {
    super(templates);
    
    this.userResource = userResource;

    this.options = options;
  }


  public Object createUser(Request request, Response response) {

    ensureAdmin(request, options.getAdminKey());

    CreateUser createDetails = parseJson(CreateUser.class, request.body());

    ensureValid(createDetails);

    String id = userResource.createUser(createDetails);

    response.redirect("/users/" + id);

    response.type(APPLICATION_JSON);

    return toJson(Status.created());
  }

  
  public Object getUser(Request request, Response response) {

    String id = request.params(":id");

    UserDetails userDetails = userResource.getDetails(id);

    // we respond as json
    response.type(APPLICATION_JSON);

    return toJson(userDetails);
  }


  public Object removeUser(Request request, Response response) {

    String id = request.params(":id");

    userResource.removeUser(id);

    // we respond as json
    response.type(APPLICATION_JSON);

    return toJson(Status.removed());
  }
  
}
