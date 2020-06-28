package io.vividcode.happyride.security;

import static io.vividcode.happyride.security.SecurityConstants.AUTHORIZATION_HEADER;
import static io.vividcode.happyride.security.SecurityConstants.TOKEN_PREFIX;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

public class JWTFilter extends BasicAuthenticationFilter {

  private final UserDetailsService userDetailsService;

  public JWTFilter(final AuthenticationManager authenticationManager,
      final UserDetailsService userDetailsService) {
    super(authenticationManager);
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {
    final String token = this.getToken(request);
    if (!StringUtils.hasText(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    final Claims claims = Jwts.parserBuilder()
        .setSigningKey(JWTKeyHolder.KEY)
        .build()
        .parseClaimsJws(token)
        .getBody();
    final UserDetails userDetails = this.userDetailsService
        .loadUserByUsername(claims.getSubject());
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(
            userDetails.getUsername(), null, userDetails.getAuthorities()
        ));
    filterChain.doFilter(request, response);
  }

  private String getToken(final HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (final Cookie cookie : request.getCookies()) {
        if (Objects.equals(cookie.getName(), SecurityConstants.AUTH_COOKIE)) {
          if (StringUtils.hasText(cookie.getValue())) {
            return cookie.getValue();
          }
        }
      }
    }
    final String header = request.getHeader(AUTHORIZATION_HEADER);
    if (header != null && header.startsWith(TOKEN_PREFIX)) {
      return header.substring(TOKEN_PREFIX.length());
    }
    return null;
  }
}
