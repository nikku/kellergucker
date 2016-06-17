package de.nixis.kk.helpers.template;

import de.nixis.kk.helpers.ApplicationException;

/**
 * Created by nikku on 6/12/16.
 */
public class TemplateRenderException extends ApplicationException {

  public TemplateRenderException(String message, Throwable cause) {
    super(message, cause, 500);
  }

}