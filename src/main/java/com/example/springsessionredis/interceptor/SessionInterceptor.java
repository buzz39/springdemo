package com.example.springsessionredis.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Do not create a new session, just check if an existing session is available
        HttpSession session = request.getSession(false);

        if (session == null) {
            System.out.println("SessionInterceptor: No session found.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session does not exist.");
            return false;
        }

        System.out.println("SessionInterceptor: Session ID = " + session.getId());
        return true;  // Continue the request if session is valid
    }
}
