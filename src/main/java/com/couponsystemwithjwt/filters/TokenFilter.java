package com.couponsystemwithjwt.filters;

import com.auth0.jwt.JWT;
import com.couponsystemwithjwt.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter {
    @Autowired
    TokenManager tokenManager;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();// return all string of URL right after the PORT!!=> must be a context path also.
       /* return path.startsWith("/my-coupon-app/auth") || path.endsWith("/coupons-for-all") || path.endsWith("/able-coupons-for-all") || path.contains("/couponForAllUsers/");*/
        return path.startsWith("/my-coupon-app/auth") || path.startsWith("/my-coupon-app/admin") || path.startsWith("/my-coupon-app/company") || path.startsWith("/my-coupon-app/customer");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String type = retrieveClientTypeFromUrl(uri);

        switch (type) {
            case "admin":
                try {
                    String token = tokenManager.returnPureToken(request);
                    Long id = JWT.decode(token).getClaim("id").asLong();
                    String email = JWT.decode(token).getClaim("email").asString();
                } catch (Exception e) {
                    response.setStatus(401);
                    response.getWriter().println("Invalid credentials! from TOKEN-FILTER"); //when I want to write body to response.
                }
                try {
                    filterChain.doFilter(request, response);//move to the next filter or to tne Controller if no more filters.
                } catch (IOException | ServletException e) {
                    //throw new RuntimeException(e.getMessage());
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
                }

                break;
            case "company":
                try {
                    String token = tokenManager.returnPureToken(request);
                    Long id = JWT.decode(token).getClaim("id").asLong();
                    String name = JWT.decode(token).getClaim("name").asString();
                    String email = JWT.decode(token).getClaim("email").asString();
                } catch (Exception e) {
                    response.setStatus(401);
                    response.getWriter().println("Invalid credentials! from TOKEN-FILTER");//when I want to write body to response.
                }
                try {
                    filterChain.doFilter(request, response);//move to the next filter or to tne Controller if no more filters.
                } catch (IOException | ServletException e) {
                    throw new RuntimeException(e.getMessage());
                }
                break;
            case "customer":
                try {
                    String token = tokenManager.returnPureToken(request);
                    Long id = JWT.decode(token).getClaim("id").asLong();
                    String firstName = JWT.decode(token).getClaim("firstName").asString();
                    String lastName = JWT.decode(token).getClaim("lastName").asString();
                    String email = JWT.decode(token).getClaim("email").asString();
                } catch (Exception e) {
                    response.setStatus(401);
                    response.getWriter().println("Invalid credentials! from TOKEN-FILTER");//when I want to write body to response.
                }
                try {
                    filterChain.doFilter(request, response);
                } catch (IOException | ServletException e) {
                    throw new RuntimeException(e.getMessage());
                }
                break;
            default:
                response.setStatus(401); //Unauthorized
                response.getWriter().println("Error from TOKEN-FILTER");
        }
    }


    private String retrieveClientTypeFromUrl(String uri) {
        if (uri.contains("admin")) {
            return "admin";
        }
        if (uri.contains("company")) {
            return "company";
        }
        if (uri.contains("customer")) {
            return "customer";
        }
        return "undefined";
    }
}


