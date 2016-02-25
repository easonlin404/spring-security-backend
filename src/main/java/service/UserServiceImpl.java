package service;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.model.User;

/**
 *
 * @author Eason Lin
 *
 */
@Repository
@Transactional
public class UserServiceImpl implements UserService {

  @PersistenceUnit
  private EntityManagerFactory emf;

  @Override
  public List<User> listUsers() {
    return emf.createEntityManager().createQuery("SELECT u FROM User u").getResultList();
  }

  @Override
  public User getUserById(String id) {
    return emf.createEntityManager().find(User.class, id);
  }

  @Override
  public User addUser(User user) {
    emf.createEntityManager().persist(user);
    return user;
  }

  @Override
  public User updateUser(User user) {
    emf.createEntityManager().persist(user);
    return user;
  }

  @Override
  public void deleteUser(String id) {
    User u = new User();
    u.setUserName(id);
    emf.createEntityManager().remove(u);

  }

}
