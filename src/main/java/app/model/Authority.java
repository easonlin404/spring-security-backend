package app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="authorities")
public class Authority implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -4921272657057761672L;

  @Id
  private String username;

  @Id
  private String authority;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public String toString() {
    return "Authority [username=" + username + ", authority=" + authority + "]";
  }


}
