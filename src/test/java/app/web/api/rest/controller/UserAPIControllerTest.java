package app.web.api.rest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import app.config.RootConfig;
import app.config.WebConfig;
import app.model.User;
import service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, WebConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
public class UserAPIControllerTest {
  @Autowired
  private WebApplicationContext context;

  private MockMvc               mvc;

  @Autowired
  private UserService userServiceMock;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .build();

  }

  @Test
  public void testListAllUsers() throws Exception {
    List<User> expectedUsers = createUsers();
    when(userServiceMock.listUsers()).thenReturn(expectedUsers);

    mvc.perform(get("/rest/user")) // Perform GET /
    .andExpect(status().isOk());
  }

  private  List<User> createUsers(){
    List<User> users = new ArrayList<User>();
    for(int i=0; i<=5; i++) {
      User user = new User();
      user.setUserName("Eason Lisn "+ i);
      user.setPassword("password"+ i);
      user.setEnabled(false);
      users.add(user);
    }
    return users;
  }
}
