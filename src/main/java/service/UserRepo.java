package service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import app.model.User;

@Transactional
public interface UserRepo extends JpaRepository <User, Long>{
}
