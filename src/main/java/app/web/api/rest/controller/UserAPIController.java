package app.web.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.model.User;
import service.UserRepo;

@RestController
@RequestMapping(value = "/rest")
public class UserAPIController {


  private UserRepo userRepo;

  @Autowired
  public UserAPIController(UserRepo userRepo) {
    this.userRepo = userRepo;
  }


  /**
   * 取得所有使用者
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  @ResponseBody
  public List<User> listAllUsers(Model model) {
    return userRepo.findAll();
  }


  /**
   *
   * @param pageNumber pageNum page number，1-indexed pages.
   * @param size 一頁幾筆
   * @param model
   * @return
   */
  @RequestMapping(value = "/user/{pageNumber}", method = RequestMethod.GET)
  @ResponseBody
  public Page<User> listAllUsersPage(@PathVariable Integer pageNumber,
      @RequestParam(value="size" ,defaultValue="20") Integer size, Model model) {
    Page<User> page = userRepo.findAll(new PageRequest(pageNumber - 1, size));

    return page;
  }

}
