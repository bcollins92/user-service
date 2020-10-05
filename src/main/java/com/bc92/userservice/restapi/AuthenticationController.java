package com.bc92.userservice.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bc92.userservice.config.UserServiceConstants;

@RestController
public class AuthenticationController {

  @PostMapping(UserServiceConstants.LOGIN_URI)
  public ResponseEntity<Void> login() {
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  @PostMapping(UserServiceConstants.LOGOUT_URI)
  public ResponseEntity<Void> logout() {
    return new ResponseEntity<Void>(HttpStatus.OK);
  }


  @GetMapping("/restricted")
  public ResponseEntity<Void> getRestricted() {
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

}
