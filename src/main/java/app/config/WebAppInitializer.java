package app.config;

import app.web.servlet.support.AbstractCustomsWebApplicationInitializer;

/**
 *
 * @author Eason Lin
 *
 */
public class WebAppInitializer
  extends AbstractCustomsWebApplicationInitializer
{


  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
  }

  /**
   * middle-tier and data-tier components.
   */
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] {RootConfig.class};
  }

  /**
   * load beans containing web components such as controllers, view resolvers,
   * and handler mappings.
   */
  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] {WebConfig.class};
  }



}
