package app.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
public class UserControllerTest {

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

    mvc.perform(get("/user")) // Perform GET /
    .andExpect(model().attribute("users", expectedUsers))
    .andExpect(view().name("user")); // Expect hello view
  }

  @Test
  public void testGetUser() throws Exception {
    User expectedUser = new User();
    expectedUser.setId("eason");
    when(userServiceMock.getUserById("eason")).thenReturn(expectedUser);

    mvc.perform(get("/user/eason")) // Perform GET /
    .andExpect(model().attribute("user", expectedUser))
    .andExpect(view().name("user")); // Expect hello view
  }

  @Test
  public void testAddUser() throws Exception {
    User expectedUser = new User();
    expectedUser.setId("eason");
    when(userServiceMock.addUser(expectedUser)).thenReturn(expectedUser);

    mvc.perform(post("/user")
        .param("id", "eason"))
        .andExpect(redirectedUrl("/user/eason"));
  }

  @Test
  public void testDeleteUser() throws Exception {
    User expectedUser = new User();
    expectedUser.setId("eason");

    mvc.perform(delete("/user/eason"))
        .andExpect(redirectedUrl("/user"));
  }

  @Test
  public void testUpdateUser() throws Exception {
    User expectedUser = new User();
    expectedUser.setId("eason");
    when(userServiceMock.updateUser(expectedUser)).thenReturn(expectedUser);

    mvc.perform(post("/user/update")
        .param("id", "eason"))
        .andExpect(redirectedUrl("/user/eason"));
  }

  private  List<User> createUsers(){
    List<User> users = new ArrayList<User>();
    for(int i=0; i<=5; i++) {
      User user = new User();
      user.setId("Eason Lisn "+ i);
      users.add(user);
    }
    return users;
  }
}
