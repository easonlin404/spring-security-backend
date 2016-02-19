package app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

/**
 *
 * @author Eason Lin
 *
 */
@EnableWebMvc
@Configuration
@ComponentScan("app.web")
public class WebConfig extends WebMvcConfigurerAdapter {



  // start Thymeleaf specific configuration
  @Bean(name = "templateResolver")
  public ServletContextTemplateResolver getTemplateResolver() {
    ServletContextTemplateResolver templateResolver =
        new ServletContextTemplateResolver();
    templateResolver.setPrefix("/WEB-INF/templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("XHTML");
    // templateResolver.setCacheTTLMs(3600000L);
    return templateResolver;
  }

  @Bean(name = "templateEngine")
  public SpringTemplateEngine getTemplateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(getTemplateResolver());

    // add Thymeleaf Layout Dialect
    templateEngine.addDialect(new LayoutDialect());
    return templateEngine;
  }

  @Bean(name = "viewResolver")
  public ThymeleafViewResolver getViewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(getTemplateEngine());
    return viewResolver;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/resources/**")
        .addResourceLocations("/resources/");
  }

}
