package io.vividcode.happyride.passengerservice.web;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter {

  @Autowired
  UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {
    final String token = this.getToken(request);
    if (token != null) {
      try {
        final Claims claims = Jwts.parserBuilder()
            .setSigningKey(JWTKeyHolder.KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
        final UserDetails userDetails = this.userDetailsService
            .loadUserByUsername(claims.getSubject());
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            ));
      } catch (final Exception e) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private String getToken(final HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (final Cookie cookie : request.getCookies()) {
        if (Objects.equals(cookie.getName(), "auth")) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
