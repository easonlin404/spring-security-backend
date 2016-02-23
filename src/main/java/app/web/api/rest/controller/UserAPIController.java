package app.web.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.model.User;
import service.UserService;

@RestController
@RequestMapping(value = "/rest")
public class UserAPIController {

  private UserService userService;

  @Autowired
  public UserAPIController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 取得所有使用者
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  @ResponseBody
  public List<User> listAllUsers(Model model) {
    return userService.listUsers();
  }

}