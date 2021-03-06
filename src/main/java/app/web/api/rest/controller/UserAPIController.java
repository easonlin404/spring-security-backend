package app.web.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
      @RequestParam(value = "size", defaultValue = "20") Integer size, Model model) {
    Page<User> page = userRepo.findAll(new PageRequest(pageNumber - 1, size));

    return page;
  }

  /**
   *
   * @param pageNumber pageNum page number，1-indexed pages.
   * @param userName 使用者姓名
   * @param size 一頁幾筆
   * @param model
   * @return
   */
  @RequestMapping(value = "/user/like/{userName}/{pageNumber}", method = RequestMethod.GET)
  @ResponseBody
  public Page<User> ListLikeUserNameUsersPage(@PathVariable Integer pageNumber,
      @PathVariable String userName,
      @RequestParam(value = "size", defaultValue = "20") Integer size, Model model) {

    Page<User> page = userRepo.findByUserNameIgnoreCaseContaining(
        userName,
        new PageRequest(pageNumber - 1, size));

    return page;
  }

  /**
   * 新增使用者
   *
   * @param user
   * @param ucBuilder
   * @return
   */
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public ResponseEntity<User> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {

    if (userRepo.exists(user.getUserName()))
      return new ResponseEntity<User>(HttpStatus.CONFLICT);

    userRepo.save(user);

    HttpHeaders headers = new HttpHeaders();
    headers
        .setLocation(ucBuilder.path("/user/{userName}").buildAndExpand(user.getUserName()).toUri());
    return new ResponseEntity<User>(user, headers, HttpStatus.CREATED);
  }

  /**
   *  更新使用者
   *
   * @param userName
   * @param user
   * @return
   */
  @RequestMapping(value = "/user/{userName}", method = RequestMethod.PUT)
  public ResponseEntity<User> updateUser(@PathVariable("userName") String userName,
      @RequestBody User user) {
    if (!userRepo.exists(userName))
      return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

    userRepo.save(user);

    return new ResponseEntity<User>(user, HttpStatus.OK);
  }

  /**
   * 刪除使用者
   *
   * @param userName
   * @return
   */
  @RequestMapping(value = "/user/{userName}", method = RequestMethod.DELETE)
  public ResponseEntity<User> deleteUser(@PathVariable("userName") String userName) {
    User user = userRepo.findOne(userName);
    if (user == null)
      return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

    userRepo.delete(user);
    return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
  }


}
