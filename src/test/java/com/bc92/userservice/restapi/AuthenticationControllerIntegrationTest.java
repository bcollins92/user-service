package com.bc92.userservice.restapi;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.bc92.userservice.config.UserServiceConstants;
import com.bc92.userservice.models.LoginRequest;
import com.bc92.userservice.utilities.Utility;

@Import(AuthenticationControllerTestConfiguration.class)
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

  @Autowired
  private MockMvc mvc;

  private LoginRequest loginRequest;

  @BeforeEach
  public void setUp() {
    loginRequest = new LoginRequest("user", "password");
  }

  // @formatter:off
  /**
   * Tests that a session can be created and destroyed
   *
   * 1. User with no token is unauthorised to access the token validation api
   * 2. User with anonymous token is unauthorised to access the token validation api
   * 3. User with valid credentials authenticates with login api and gets a cookie/token
   * 4. User uses said token to authenticate and access token validation api
   * 5. User revokes token using the logout api
   * 6. User with revoked token is unauthorised to access the token validation api
   *
   * @throws Exception
   */
  @Test
  public void testSessionIsCreatedAndDestroyed() throws Exception {
    MvcResult mvcResult;
    Cookie cookie;

    // 1. No token
    mvcResult = mvc.perform(MockMvcRequestBuilders
        .get(UserServiceConstants.VALIDATE_TOKEN_URL))
        .andExpect(status().isForbidden()).andReturn();

    // 2. Anonymous token
    cookie = mvcResult.getResponse().getCookie(UserServiceConstants.COOKIE_NAME);
    mvc.perform(MockMvcRequestBuilders
        .get(UserServiceConstants.VALIDATE_TOKEN_URL)
        .cookie(cookie))
        .andExpect(status().isForbidden());

    // 3. Login
    mvcResult = mvc.perform(MockMvcRequestBuilders
                              .post(UserServiceConstants.LOGIN_URL)
                              .content(Utility.objectToJson(loginRequest)))
                              .andExpect(status().isOk())
                              .andReturn();

    // 4. Valid Token
    cookie = mvcResult.getResponse().getCookie(UserServiceConstants.COOKIE_NAME);
    mvc.perform(MockMvcRequestBuilders
        .get(UserServiceConstants.VALIDATE_TOKEN_URL)
        .cookie(cookie))
        .andExpect(status().isOk());

    // 5. Logout
    mvc.perform(MockMvcRequestBuilders
        .get(UserServiceConstants.LOGOUT_URL)
        .cookie(cookie))
        .andExpect(status().isOk());

    // 6. Invalidated token
    mvc.perform(MockMvcRequestBuilders
        .get(UserServiceConstants.VALIDATE_TOKEN_URL)
        .cookie(cookie))
        .andExpect(status().isForbidden());

  }

}
