package service;

import java.util.List;

import app.model.User;

public interface UserService {

  public List<User> listUsers();

  public User getUserById(String id);

  public User addUser(User user);
  public User updateUser(User user);

  public void deleteUser(String id);
}
