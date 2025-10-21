package com.connectfood.core.infrastructure.security;

import java.io.IOException;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }

    if (SecurityContextHolder.getContext()
        .getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = resolveBearerToken(request);
    if (token != null && jwtService.validate(token)) {
      String username = jwtService.getSubject(token);
      var userDetails = userDetailsService.loadUserByUsername(username);

      var auth = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext()
          .setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }

  @Nullable
  private String resolveBearerToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header == null) {
      return null;
    }
    if (!header.regionMatches(true, 0, "Bearer ", 0, 7)) {
      return null;
    }
    if (header.length() <= 7) {
      return null;
    }
    return header.substring(7)
        .trim();
  }
}
