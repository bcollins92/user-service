package com.bc92.userservice.models;

public class LoginRequest {
  private String user;
  private String password;

  public LoginRequest() {
    // empty
  }

  public LoginRequest(final String user, final String password) {
    this.user = user;
    this.password = password;
  }

  public String getUser() {
    return user;
  }

  public void setUser(final String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

}
