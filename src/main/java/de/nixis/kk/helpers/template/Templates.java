package de.nixis.kk.helpers.template;

import freemarker.cache.CacheStorage;
import freemarker.cache.MruCacheStorage;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Locale;
import lombok.Setter;
import lombok.experimental.Accessors;
import spark.ModelAndView;


@Accessors(chain = true)
public class Templates {

  private static final Version FREEMARKER_VERSION = Configuration.getVersion();
  private static final String FREEMARKER_EXTENSION = ".ftl";

  private final Configuration configuration;

  public Templates() {

    configuration = new Configuration(FREEMARKER_VERSION);

    configuration.loadBuiltInEncodingMap();

    configuration.setObjectWrapper(new DefaultObjectWrapperBuilder(FREEMARKER_VERSION).build());
    configuration.setDefaultEncoding("UTF-8");
    configuration.setClassForTemplateLoading(Templates.class, "/templates");
  }

  public String render(ModelAndView modelAndView) {
    return render(modelAndView, defaultLocale);
  }

  public String render(ModelAndView modelAndView, Locale locale) {
    try {
      Charset charset = Charset.forName(configuration.getEncoding(locale));

      String viewName = modelAndView.getViewName();

      if (!viewName.endsWith(FREEMARKER_EXTENSION)) {
        viewName += FREEMARKER_EXTENSION;
      }

      Template template = configuration.getTemplate(viewName, locale, charset.name());

      StringWriter writer = new StringWriter();

      template.process(modelAndView.getModel(), writer);

      return writer.toString();
    } catch (TemplateNotFoundException e) {
      throw new TemplateRenderException("template not found", e);
    } catch (MalformedTemplateNameException e) {
      throw new TemplateRenderException("template name error", e);
    } catch (TemplateException e) {
      throw new TemplateRenderException("template render error", e);
    } catch (IOException e) {
      throw new TemplateRenderException("template io error", e);
    }
  }

  @Setter
  private Locale defaultLocale = Locale.getDefault();

  /**
   * Create freemarker configuration.
   */
  public Templates setCache(boolean enabled) {

    CacheStorage cache;

    if (enabled) {
      cache = new MruCacheStorage(300, 200);
    } else {
      cache = new NullCacheStorage();
    }

    configuration.setCacheStorage(cache);

    return this;
  }

  public void destroy() {
    this.configuration.unsetCacheStorage();
  }
}
