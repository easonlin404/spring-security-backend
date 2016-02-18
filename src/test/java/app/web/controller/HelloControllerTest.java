package app.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * HelloController TestCase
 *
 * <p>
 * Test HelloController
 *
 * @author Eason
 *
 */
public class HelloControllerTest {

  @Test
  public void testIndexPage() throws Exception {
    HelloController controller = new HelloController();
    MockMvc mockMvc = standaloneSetup(controller).build();
    mockMvc.perform(get("/")) // Perform GET /
        .andExpect(model().attribute("title", "Spring Security Hello World"))
        .andExpect(model().attribute("message", "This is welcome page!"))
        .andExpect(view().name("hello")); // Expect hello view
  }

  @Test
  public void testWelcomePage() throws Exception {
    HelloController controller = new HelloController();
    MockMvc mockMvc = standaloneSetup(controller).build();
    mockMvc.perform(get("/welcome")) // Perform GET /
        .andExpect(model().attribute("title", "Spring Security Hello World"))
        .andExpect(model().attribute("message", "This is welcome page!"))
        .andExpect(view().name("hello")); // Expect hello view
  }

  @Test
  public void testAdminPage() throws Exception {
    HelloController controller = new HelloController();
    MockMvc mockMvc = standaloneSetup(controller)
        .setSingleView(new InternalResourceView("/WEB-INF/views/admin.html")).build();

    mockMvc.perform(get("/admin")) // Perform GET /
    .andExpect(model().attribute("title", "Spring Security Hello World"))
    .andExpect(model().attribute("message", "This is protected page!"))
        .andExpect(view().name("admin")); // Expect hello view
  }

}
