package com.bc92.userservice.authn;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;
import com.bc92.projectsdk.utils.JsonUtilities;
import com.bc92.userservice.models.LoginRequest;

/**
 * Extends UsernamePasswordAuthenticationFilter to convert the request body into a LoginRequest
 * object and get login details from that
 *
 * @author Brian
 *
 */
public class CustomUsernamePasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

  private static final Logger logger =
      LoggerFactory.getLogger(CustomUsernamePasswordAuthenticationFilter.class);

  private LoginRequest loginRequest;

  @Override
  protected String obtainUsername(final HttpServletRequest request) {
    logger.trace(">> obtainUsername()");
    this.setLoginRequest(request);
    logger.trace("<< obtainUsername()");
    return loginRequest.getUser();
  }

  @Override
  protected String obtainPassword(final HttpServletRequest request) {
    logger.trace(">><< obtainPassword()");
    return loginRequest.getPassword();
  }

  private void setLoginRequest(final HttpServletRequest request) {
    logger.trace(">> setLoginRequest()");
    try {

      StringBuilder builder = new StringBuilder();
      BufferedReader reader = request.getReader();
      String currentLine = "";

      while ((currentLine = reader.readLine()) != null) {
        builder.append(currentLine);
      }

      loginRequest = JsonUtilities.jsonToObject(builder.toString(), LoginRequest.class);
      logger.debug("Login Request for user {} recieved.", loginRequest.getUser());

    } catch (IOException e) {

      logger.error("RequestBody for Login failed to be read", e);
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
          "RequestBody for Login failed to be read");
    }

    logger.trace("<< setLoginRequest()");
  }

}
