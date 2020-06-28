package io.vividcode.happyride.security;

import static io.vividcode.happyride.security.SecurityConstants.AUTHORIZATION_HEADER;
import static io.vividcode.happyride.security.SecurityConstants.TOKEN_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public JWTAuthenticationFilter(
      final AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest request,
      final HttpServletResponse response) throws AuthenticationException {
    try {
      final LoginRequest loginRequest = this.objectMapper
          .readValue(request.getInputStream(), LoginRequest.class);
      return this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(),
              loginRequest.getPassword()));
    } catch (final IOException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain chain,
      final Authentication authResult) {
    final String token = Jwts.builder()
        .setSubject(((User) authResult.getPrincipal()).getUsername())
        .setIssuedAt(new Date())
        .setExpiration(Date.from(OffsetDateTime.now().plusDays(5).toInstant()))
        .signWith(JWTKeyHolder.KEY, SignatureAlgorithm.HS512)
        .compact();
    response.addHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + token);
    response.addCookie(new Cookie(SecurityConstants.AUTH_COOKIE, token));
  }
}
