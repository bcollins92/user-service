package com.bc92.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.header.HeaderWriterFilter;
import com.bc92.userservice.security.LoginEndpointFilter;

/**
 * This class allows us to provide our own authentication configuration, buy overriding methods
 * which configure
 *
 * @author Brian
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  //@formatter:off
  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(UserServiceConstants.LOGIN_URI)
        .permitAll()
        .antMatchers("/**")
        .authenticated()
        .and()
        .addFilterAfter(new LoginEndpointFilter(this.authenticationProvider(), UserServiceConstants.LOGIN_URI), HeaderWriterFilter.class);

    http.formLogin().disable();
   // http.logout().disable();
    http.csrf().disable();
  }


  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    UserDetails user = User.builder()
                          .username("user")
                          .password(this.passwordEncoder().encode("password"))
                          .roles("USER")
                          .build();

    return new InMemoryUserDetailsManager(user);
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(this.userDetailsService())
        .passwordEncoder(this.passwordEncoder())
        .and()
        .authenticationProvider(this.authenticationProvider());
  }
  //@formatter:on

  // @Bean
  // public HttpSessionIdResolver httpSessionIdResolver() {
  // return HeaderHttpSessionIdResolver.authenticationInfo();
  // }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(this.userDetailsService());
    authProvider.setPasswordEncoder(this.passwordEncoder());
    return authProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
