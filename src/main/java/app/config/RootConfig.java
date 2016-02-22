package app.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import app.service.UserService;


/**
 *
 * @author Eason Lin
 *
 */
@Configuration
@ComponentScan(basePackages = {"app"},
    excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
public class RootConfig {

  @Bean
  public UserService UserService() {
    return Mockito.mock(UserService.class);
  }
}
