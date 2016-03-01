package service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import app.model.User;

@Transactional
public interface UserRepo extends JpaRepository <User, String>{

  Page<User> findByUserNameIgnoreCaseContaining(String userName, Pageable pageable);

}
