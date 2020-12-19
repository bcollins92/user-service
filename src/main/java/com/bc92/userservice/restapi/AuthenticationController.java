package com.bc92.userservice.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bc92.projectsdk.constants.UserServiceConstants;

@RestController
public class AuthenticationController {

  @PostMapping(UserServiceConstants.LOGIN_PATH)
  public ResponseEntity<Void> login() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(UserServiceConstants.LOGOUT_PATH)
  public ResponseEntity<Void> logout() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(UserServiceConstants.VALIDATE_TOKEN_PATH)
  public ResponseEntity<Void> validateToken() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
