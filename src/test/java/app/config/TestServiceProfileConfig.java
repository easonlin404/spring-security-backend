package app.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import service.UserRepo;
import service.UserService;

@Configuration
@Profile("test")
public class TestServiceProfileConfig {


  @Bean
  public UserService UserService() {
    return Mockito.mock(UserService.class);
  }

  @Bean
  public UserRepo UserRepo(){
    return Mockito.mock(UserRepo.class);
  }
}
