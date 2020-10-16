package com.bc92.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessEventPublishingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.bc92.userservice.authn.CustomUsernamePasswordAuthenticationFilter;
import com.bc92.userservice.authn.NoOpLoginSuccessHandler;
import com.bc92.userservice.authn.NoOpLogoutSuccessHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  //@formatter:off
  @Override
  protected void configure(final HttpSecurity http) throws Exception {

    http
      .authorizeRequests()
      .antMatchers("/**")
      .authenticated()
    .and()
      .addFilterAfter(this.loginFilter(this.authenticationManagerBean()), HeaderWriterFilter.class)
      .addFilterAfter(this.logoutFilter(), HeaderWriterFilter.class);

    http.csrf().disable();
    http.logout().disable();

  }

  private LogoutFilter logoutFilter() {
    LogoutFilter logoutFilter = new LogoutFilter(new NoOpLogoutSuccessHandler(), new SecurityContextLogoutHandler(),
        new LogoutSuccessEventPublishingLogoutHandler());
    logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher(UserServiceConstants.LOGOUT_URL, HttpMethod.GET.name()));
    return logoutFilter;
  }

  public UsernamePasswordAuthenticationFilter loginFilter(final AuthenticationManager authenticationManager) {
    CustomUsernamePasswordAuthenticationFilter loginFilter = new CustomUsernamePasswordAuthenticationFilter();
    loginFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(UserServiceConstants.LOGIN_URL, HttpMethod.POST.name()));
    loginFilter.setAuthenticationManager(authenticationManager);
    loginFilter.setAuthenticationSuccessHandler(new NoOpLoginSuccessHandler());
    return loginFilter;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return new ProviderManager(this.authenticationProvider());
  }


  @Bean
  @Override
  public UserDetailsService userDetailsServiceBean() {
    UserDetails user = User.builder()
                          .username("user")
                          .password(this.passwordEncoder().encode("password"))
                          .roles("USER")
                          .build();

    return new InMemoryUserDetailsManager(user);
  }

  //@formatter:on

  @Bean
  public AuthenticationProvider authenticationProvider() {
    final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(this.userDetailsServiceBean());
    authProvider.setPasswordEncoder(this.passwordEncoder());
    return authProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
