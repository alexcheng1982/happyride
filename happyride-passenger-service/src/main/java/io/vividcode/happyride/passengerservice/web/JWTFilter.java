package io.vividcode.happyride.passengerservice.web;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static io.vividcode.happyride.passengerservice.web.SecurityConstants.AUTHORIZATION_HEADER;
import static io.vividcode.happyride.passengerservice.web.SecurityConstants.TOKEN_PREFIX;

public class JWTFilter extends BasicAuthenticationFilter {


  public JWTFilter(final AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
                                  final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {
    final String token = this.getToken(request);
    if (StringUtils.hasText(token)) {
      try {
        final Claims claims = Jwts.parserBuilder()
            .setSigningKey(JWTKeyHolder.KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null
            ));
      } catch (final Exception e) {
        SecurityContextHolder.clearContext();
        response.addCookie(new Cookie(SecurityConstants.AUTH_COOKIE, ""));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private String getToken(final HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (final Cookie cookie : request.getCookies()) {
        if (Objects.equals(cookie.getName(), SecurityConstants.AUTH_COOKIE)) {
          return cookie.getValue();
        }
      }
    }
    final String header = request.getHeader(AUTHORIZATION_HEADER);
    if (header != null && header.startsWith(TOKEN_PREFIX)) {
      return header;
    }
    return null;
  }
}
