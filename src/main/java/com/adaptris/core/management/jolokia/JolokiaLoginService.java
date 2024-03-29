package com.adaptris.core.management.jolokia;

import java.util.List;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Password;

import com.adaptris.security.exc.PasswordException;

public class JolokiaLoginService extends AbstractLoginService implements LoginService {

  private final String username;
  private final String password;

  public JolokiaLoginService(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  protected List<RolePrincipal> loadRoleInfo(UserPrincipal user) {
    if (username.equals(user.getName())) {
      return List.of(new RolePrincipal("jolokia"));
    }
    return List.of();
  }

  @Override
  protected UserPrincipal loadUserInfo(String username) {
    if (this.username.equals(username)) {
      return new UserPrincipal(username, new Password(decode(password)));
    }
    return null;
  }

  private String decode(String password) {
    try {
      return com.adaptris.security.password.Password.decode(password);
    } catch (PasswordException pwe) {
      throw new IllegalStateException(pwe);
    }
  }

}
