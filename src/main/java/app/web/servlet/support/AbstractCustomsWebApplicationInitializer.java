package app.web.servlet.support;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * CustomWebApplicationInitializer
 *
 * TODO:
 * 自訂 WebApplicationInitializer，為了之後要能自訂Spring Security
 *
 * @author Eason Lin
 *
 */
public abstract class AbstractCustomsWebApplicationInitializer
    implements WebApplicationInitializer
{

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    registerContextLoaderListener(servletContext);
    registerDispatcherServlet(servletContext);
  }


  protected void registerContextLoaderListener(ServletContext servletContext) {
    WebApplicationContext rootAppContext = createRootApplicationContext();
    Assert.notNull(rootAppContext);
    ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
    // listener.setContextInitializers(getRootApplicationContextInitializers());
    servletContext.addListener(listener);
  }

  protected void registerDispatcherServlet(ServletContext servletContext) {
    WebApplicationContext servletAppContext = createServletApplicationContext();
    FrameworkServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
    dispatcherServlet.setContextInitializers(getServletApplicationContextInitializers());

    ServletRegistration.Dynamic registration = servletContext.addServlet("servletDispatcher", dispatcherServlet);

    registration.setLoadOnStartup(1);
    registration.addMapping(getServletMappings());
    registration.setAsyncSupported(true);

  }


  private WebApplicationContext createRootApplicationContext() {
    Class<?>[] configClasses = getRootConfigClasses();
    AnnotationConfigWebApplicationContext rootAppContext =
        new AnnotationConfigWebApplicationContext();
    rootAppContext.register(configClasses);
    return rootAppContext;
  }

  private WebApplicationContext createServletApplicationContext() {
    AnnotationConfigWebApplicationContext servletAppContext =
        new AnnotationConfigWebApplicationContext();
    Class<?>[] configClasses = getServletConfigClasses();
    if (!ObjectUtils.isEmpty(configClasses)) {
    servletAppContext.register(configClasses);
    }
    return servletAppContext;
  }

  protected abstract Class<?>[] getRootConfigClasses();

  protected abstract Class<?>[] getServletConfigClasses();

  protected abstract String[] getServletMappings();

  protected ApplicationContextInitializer<?>[] getServletApplicationContextInitializers() {
    return null;
  }
}
