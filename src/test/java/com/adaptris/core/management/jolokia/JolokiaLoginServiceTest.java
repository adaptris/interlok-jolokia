package com.adaptris.core.management.jolokia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;
import org.junit.jupiter.api.Test;

import com.adaptris.security.exc.PasswordException;
import com.adaptris.security.password.Password;

public class JolokiaLoginServiceTest {

  @Test
  public void testLoadRoleInfo() throws PasswordException {
    JolokiaLoginService jolokiaLoginService = newJolokiaLoginService();

    List<RolePrincipal> roles = jolokiaLoginService.loadRoleInfo(new UserPrincipal("username", null));

    assertNotNull(roles);
    assertEquals("jolokia", roles.get(0).getName());
  }

  @Test
  public void testLoadRoleInfoInvalidUsername() throws PasswordException {
    JolokiaLoginService jolokiaLoginService = newJolokiaLoginService();

    List<RolePrincipal> roles = jolokiaLoginService.loadRoleInfo(new UserPrincipal("invalid", null));

    assertTrue(roles.isEmpty());
  }

  @Test
  public void testLoadUserInfo() throws PasswordException {
    JolokiaLoginService jolokiaLoginService = newJolokiaLoginService();

    UserPrincipal userPrincipal = jolokiaLoginService.loadUserInfo("username");

    assertNotNull(userPrincipal);
    assertEquals("username", userPrincipal.getName());
    assertTrue(userPrincipal.authenticate(Credential.getCredential("password")));
  }

  @Test
  public void testLoadUserInfoInvalidUsername() throws PasswordException {
    JolokiaLoginService jolokiaLoginService = newJolokiaLoginService();

    UserPrincipal userPrincipal = jolokiaLoginService.loadUserInfo("invalid");

    assertNull(userPrincipal);
  }

  @Test
  public void testLoadUserInfoInvalidInitialPassword() throws PasswordException {
    JolokiaLoginService jolokiaLoginService = new JolokiaLoginService("username", "PW:invalid");

    assertThrows(IllegalStateException.class, () -> {
      jolokiaLoginService.loadUserInfo("username");
    });
  }

  @Test
  public void testLogin() throws PasswordException {
    JolokiaLoginService jolokiaLoginService = newJolokiaLoginService();

    UserIdentity userIdentity = jolokiaLoginService.login("username", Credential.getCredential("password"), null);

    assertNotNull(userIdentity);
  }

  @Test
  public void testLoginInvalidCredential() throws PasswordException {
    JolokiaLoginService jolokiaLoginService = newJolokiaLoginService();

    UserIdentity userIdentity = jolokiaLoginService.login("username", Credential.getCredential("invalid"), null);

    assertNull(userIdentity);
  }

  private JolokiaLoginService newJolokiaLoginService() throws PasswordException {
    return new JolokiaLoginService("username", Password.encode("password", Password.PORTABLE_PASSWORD));
  }

}
