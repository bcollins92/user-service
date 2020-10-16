package com.bc92.userservice.authn;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.bc92.userservice.config.UserServiceConstants;
import com.bc92.userservice.models.LoginRequest;
import com.bc92.userservice.utilities.Utility;

public class LoginEndpointFilter extends OncePerRequestFilter {

  private final static Logger logger = LoggerFactory.getLogger(LoginEndpointFilter.class);


  private final AuthenticationProvider authenticationProvidor;

  public LoginEndpointFilter(final AuthenticationProvider authenticationProvidor) {

    this.authenticationProvidor = authenticationProvidor;
  }

  /**
   * Should not filter if request is not a POST on the login URL
   *
   */
  @Override
  protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
    return !(HttpMethod.POST.name().equals(request.getMethod())
        && UserServiceConstants.LOGIN_URL.equals(request.getRequestURI()));
  }

  // TODO - log failed attempts to login, for security monitoring
  // TODO - improve logging - copy the logging in BasicAuthenticationFilter:
  /*
   * o.s.s.w.a.w.BasicAuthenticationFilter - Basic Authentication Authorization header found for
   * user 'user' 07:42:43.456 [http-nio-8080-exec-6] DEBUG o.s.s.authentication.ProviderManager -
   * Authentication attempt using
   * org.springframework.security.authentication.dao.DaoAuthenticationProvider 07:42:43.543
   * [http-nio-8080-exec-6] DEBUG o.s.s.w.a.w.BasicAuthenticationFilter - Authentication success: ..
   * token, principle
   */
  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {

    logger.debug(">> doFilterInternal()");

    LoginRequest loginRequest = this.getLoginRequest(request);

    // if Authentication fails, errors will be thrown here
    // security content will not be populated
    // Springs default errors are sufficient for now
    Authentication auth =
        authenticationProvidor.authenticate(new UsernamePasswordAuthenticationToken(
            loginRequest.getUser(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(auth);

    filterChain.doFilter(request, response);
  }

  private LoginRequest getLoginRequest(final HttpServletRequest request) throws IOException {

    StringBuilder builder = new StringBuilder();
    BufferedReader reader = request.getReader();
    String currentLine = "";

    while ((currentLine = reader.readLine()) != null) {
      builder.append(currentLine);
      builder.append(System.lineSeparator());
    }

    return Utility.jsonToObject(builder.toString(), LoginRequest.class);
  }

}
