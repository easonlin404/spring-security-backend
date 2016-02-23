package app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.User;
import service.UserService;

/**
 *
 * @author Eason
 *
 * 使用者管理 Controller
 */
@Controller
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }


  @RequestMapping(value = "/userManagement", method = RequestMethod.GET)
  public String UserManagementPage(){
    return "userManagement";
  }
  /**
   * 取得所有使用者
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public Model listAllUsers(Model model) {
    model.addAttribute("users", userService.listUsers());
    return model;
  }

  /**
   * 取得使用者
   *
   * @param id 使用者代碼
   */
  @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
  public String getUser(@PathVariable String id, Model model) {
    model.addAttribute("user", userService.getUserById(id));
    return "user";
  }


  /**
   * 新增使用者
   *
   * @param user 使用者model
   * @return
   */
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public String addUser(@ModelAttribute User user) {
    userService.addUser(user);
    return "redirect:/user/" + user.getId();
  }


  /**
   * 刪除使用者
   *
   * @param id 使用者帳號
   */
  @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
  public String deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return "redirect:/user";
  }

  /**
   * 修改使用者
   *
   * @param user 使用者model
   * @return
   */
  @RequestMapping(value = "/user/update", method = RequestMethod.POST)
  public String updateUser(User user) {
    userService.updateUser(user);
    return "redirect:/user/" + user.getId();
  }
}
