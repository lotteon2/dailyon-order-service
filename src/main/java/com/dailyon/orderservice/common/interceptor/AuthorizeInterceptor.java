package com.dailyon.orderservice.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizeInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (!isAuthorized(request)) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return false;
    }

    return true;
  }

  private boolean isAuthorized(HttpServletRequest request) {
    String role = request.getHeader("role");
    return role != null && role.equals("ROLE_ADMIN");
  }
}
