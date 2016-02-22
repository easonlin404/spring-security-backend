package app.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import service.UserService;

@Configuration
@Profile("test")
public class TestServiceProfileConfig {


  @Bean
  public UserService UserService() {
    return Mockito.mock(UserService.class);
  }
}
