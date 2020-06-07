package io.vividcode.happyride.passengerservice.web;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class JWTSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(final HttpServletRequest request,
      final HttpServletResponse response, final Authentication authentication)
      throws IOException, ServletException {
    final String token = Jwts.builder()
        .setSubject(((User) authentication.getPrincipal()).getUsername())
        .setIssuedAt(new Date())
        .setExpiration(Date.from(OffsetDateTime.now().plusDays(5).toInstant()))
        .signWith(JWTKeyHolder.KEY, SignatureAlgorithm.HS512)
        .compact();
    response.addCookie(new Cookie("auth", token));
    request.getRequestDispatcher("/").forward(request, response);
  }
}
