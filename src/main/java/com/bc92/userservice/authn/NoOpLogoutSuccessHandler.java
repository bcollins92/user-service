package com.bc92.userservice.authn;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Performs no url Redirects because current API goals for this project are to be useable by postman
 *
 * @author Brian
 *
 */
public class NoOpLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
      final Authentication authentication) throws IOException, ServletException {
    // Zilch
  }

}
