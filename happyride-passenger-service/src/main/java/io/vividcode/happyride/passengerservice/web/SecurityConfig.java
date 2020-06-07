package io.vividcode.happyride.passengerservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  JWTFilter jwtFilter;

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf().disable()
        .formLogin().successHandler(new JWTSuccessHandler())
        .and()
        .authorizeRequests()
        .antMatchers("/login").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(this.jwtFilter,
            UsernamePasswordAuthenticationFilter.class)
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth)
      throws Exception {
    auth.inMemoryAuthentication()
        .withUser("admin")
        .password(this.passwordEncoder().encode("password"))
        .roles("ADMIN")
        .and().withUser("user")
        .password(this.passwordEncoder().encode("password"))
        .roles("USER");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
