package com.bc92.userservice.restapi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import com.bc92.userservice.config.WebSecurityConfig;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

/**
 * Test configuration to provide an embedded instance of Redis and UserDetailsService to allow
 * authentication to be tested with no external dependencies
 *
 * @author Brian
 *
 */
@TestConfiguration
@Order(101)
public class AuthenticationControllerTestConfiguration extends WebSecurityConfig {

  private final static Logger logger =
      LoggerFactory.getLogger(AuthenticationControllerTestConfiguration.class);

  private final RedisServer redisServer;

  public AuthenticationControllerTestConfiguration(
      @Value("${spring.redis.port}") final int redisPort,
      @Value("${spring.redis.host}") final String redisHost) {
    logger.info("Constructor. redisPort: {}, redisHost: {}", redisPort, redisHost);

    redisServer = new RedisServerBuilder().port(redisPort).setting("maxheap 64M").build();
  }

  @PostConstruct
  public void postConstruct() {
    redisServer.start();
  }

  @PreDestroy
  public void preDestroy() {
    redisServer.stop();
  }

  //@formatter:off
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
}
