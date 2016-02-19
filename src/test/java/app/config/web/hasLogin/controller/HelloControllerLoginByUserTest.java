package app.config.web.hasLogin.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import app.config.RootConfig;
import app.config.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, WebConfig.class})
@WebAppConfiguration
public class HelloControllerLoginByUserTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc               mvc;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .defaultRequest(get("/").with(user("user").roles("USER")))
        .apply(springSecurity())
        .build();
  }

  @Test
  public void testIndexPage() throws Exception {
    mvc.perform(get("/")) // Perform GET /
        .andExpect(model().attribute("title", "Spring Security Hello World"))
        .andExpect(model().attribute("message", "This is welcome page!"))
        .andExpect(view().name("hello")); // Expect hello view
  }

  @Test
  public void testWelcomePage() throws Exception {
    mvc.perform(get("/welcome")) // Perform GET /
        .andExpect(model().attribute("title", "Spring Security Hello World"))
        .andExpect(model().attribute("message", "This is welcome page!"))
        .andExpect(view().name("hello")); // Expect hello view
  }

  @Test
  public void testAdminPageForbidden() throws Exception {
    //user沒有權限access /admin
    mvc.perform(get("/admin")) // Perform GET /
        .andExpect(status().isForbidden());
  }

}

