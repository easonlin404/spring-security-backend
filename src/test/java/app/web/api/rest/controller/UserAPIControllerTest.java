package app.web.api.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

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

  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

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

    mvc.perform(get("/rest/user/1?size=20")) // Perform GET /rest/user/1?size=20
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.number", is(0)))
    .andExpect(jsonPath("$.last", is(true)))
    .andExpect(jsonPath("$.size", is(0)))
    .andExpect(jsonPath("$.content", hasSize(6)))
    .andExpect(jsonPath("$.content[0].userName", is("Eason Lisn 0")))
    .andExpect(jsonPath("$.content[0].password", is("password0")))
    .andExpect(jsonPath("$.content[0].enabled", is(false)));
  }

  @Test
  public void testcCeateUserSuccess() throws Exception {
    User expectUser = new User();
    expectUser.setUserName("eason");
    expectUser.setPassword("1234567890");
    expectUser.setEnabled(true);

    when(userRepo.exists("eason")).thenReturn(false);
    when(userRepo.save(expectUser)).thenReturn(expectUser);

    mvc.perform(post("/rest/user")   //Perform POST /rest/user
        .contentType(APPLICATION_JSON_UTF8)
        .content(convertObjectToJsonString(expectUser)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", is("http://localhost/user/eason")))
        .andExpect(jsonPath("$.userName", is(expectUser.getUserName())))
        .andExpect(jsonPath("$.password", is(expectUser.getPassword())))
        .andExpect(jsonPath("$.enabled", is(expectUser.isEnabled())))
        .andDo(print());
  }

  @Test
  public void testcCeateUserFail() throws Exception {
    User expectUser = new User();
    expectUser.setUserName("eason");
    expectUser.setPassword("1234567890");
    expectUser.setEnabled(true);
    when(userRepo.exists("eason")).thenReturn(true);

    mvc.perform(post("/rest/user")   //Perform POST /rest/user
        .contentType(APPLICATION_JSON_UTF8)
        .content(convertObjectToJsonString(expectUser)))
        .andDo(print())
        .andExpect(status().isConflict());



  }

  @Test
  public void testUpdateUserSuccess() throws Exception {
    User expectUser = new User();
    expectUser.setUserName("eason");
    expectUser.setPassword("1234567890");
    expectUser.setEnabled(true);

    when(userRepo.exists("eason")).thenReturn(true);
    when(userRepo.save(expectUser)).thenReturn(expectUser);

    mvc.perform(put("/rest/user/eason")   //Perform PUT /rest/user
        .contentType(APPLICATION_JSON_UTF8)
        .content(convertObjectToJsonString(expectUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName", is(expectUser.getUserName())))
        .andExpect(jsonPath("$.password", is(expectUser.getPassword())))
        .andExpect(jsonPath("$.enabled", is(expectUser.isEnabled())));
  }

  @Test
  public void testUpdateUserFail() throws Exception {
    User expectUser = new User();
    expectUser.setUserName("eason");
    expectUser.setPassword("1234567890");
    expectUser.setEnabled(true);

    when(userRepo.exists("eason")).thenReturn(false);

    mvc.perform(put("/rest/user/eason")   //Perform PUT /rest/user/eason
        .contentType(APPLICATION_JSON_UTF8)
        .content(convertObjectToJsonString(expectUser)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteUserSuccess() throws Exception {
    User expectUser = new User();
    expectUser.setUserName("eason");
    expectUser.setPassword("1234567890");
    expectUser.setEnabled(true);

    when(userRepo.findOne("eason")).thenReturn(expectUser);

    mvc.perform(delete("/rest/user/eason")) // Perform DELETE /rest/user/eason
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteUserFail() throws Exception {
    when(userRepo.findOne("eason")).thenReturn(null);

    mvc.perform(delete("/rest/user/eason")) // Perform DELETE /rest/user/eason
        .andExpect(status().isNotFound());
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

  private  String convertObjectToJsonString(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper.writeValueAsString(object);
}
}
