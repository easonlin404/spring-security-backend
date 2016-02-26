package app.web.api.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import service.UserRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, WebConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
public class UserAPIControllerTest {
  @Autowired
  private WebApplicationContext context;

  private MockMvc               mvc;

  @Autowired
  private UserRepo userRepo;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .build();

  }

  @Test
  public void testListAllUsers() throws Exception {
    List<User> expectedUsers = createUsers();
    when(userRepo.findAll()).thenReturn(expectedUsers);

    mvc.perform(get("/rest/user")) // Perform GET /
    .andExpect(status().isOk());
  }

  @Test
  public void testListAllUsersPage() throws Exception {
    int page = 1;
    List<User> expectedUsers = createUsers();

    Page<User> p  = new PageImpl<User>(expectedUsers);

    when(userRepo.findAll(new PageRequest(page - 1, 20))).thenReturn(p);

    mvc.perform(get("/rest/user/1?size=20")) // Perform GET /
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.number", is(0)))
    .andExpect(jsonPath("$.last", is(true)))
    .andExpect(jsonPath("$.size", is(0)))
    .andExpect(jsonPath("$.content", hasSize(6)))
    .andExpect(jsonPath("$.content[0].userName", is("Eason Lisn 0")))
    .andExpect(jsonPath("$.content[0].password", is("password0")))
    .andExpect(jsonPath("$.content[0].enabled", is(false)));



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
