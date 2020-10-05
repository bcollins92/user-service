package com.bc92.userservice.models;

public class LoginRequest {
  private String user;
  private String password;

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

  @Override
  public String toString() {
    return "LoginRequest [user=" + user + ", password=" + password + "]";
  }
}
