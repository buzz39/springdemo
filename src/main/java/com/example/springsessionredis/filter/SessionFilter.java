package com.example.springsessionredis.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class SessionFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Generate a unique request ID to trace each request
        String requestId = UUID.randomUUID().toString();
        String dispatcherType = request.getDispatcherType().name();  // Get the request dispatcher type

        // Log the request path, HTTP method, and dispatcher type along with the unique request ID
        String path = request.getRequestURI();
        String method = request.getMethod();
        System.out.println("SessionFilter: Request ID = " + requestId + ", Request Path = " + path);
        System.out.println("SessionFilter: Request ID = " + requestId + ", HTTP Method = " + method);
        System.out.println("SessionFilter: Request ID = " + requestId + ", Dispatcher Type = " + dispatcherType);

        HttpSession session = request.getSession(false);  // Don't create a new session if it doesn't exist

        if (session != null) {
            System.out.println("SessionFilter: Request ID = " + requestId + ", Session ID = " + session.getId());
        } else {
            System.out.println("SessionFilter: Request ID = " + requestId + ", No session found.");
        }

        chain.doFilter(request, response);  // Continue with the request
    }
}
