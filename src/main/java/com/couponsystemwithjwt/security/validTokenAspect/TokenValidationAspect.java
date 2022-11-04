package com.couponsystemwithjwt.security.validTokenAspect;

import com.auth0.jwt.JWT;
import com.couponsystemwithjwt.services.ClientService;
import com.couponsystemwithjwt.state.MySession;
import org.apache.tomcat.websocket.AuthenticationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;

@Component
@Aspect
public class TokenValidationAspect {

    @Autowired
    private HashMap<Long, MySession> sessionHashMap;

    @Around("@annotation(ValidToken)")
    public Object validate(ProceedingJoinPoint originalMethod) throws Throwable {

        System.out.println("running the proxy!");

        Object[] params = originalMethod.getArgs();
        boolean valid = false;
        for (Object param : params) {
            if (param instanceof HttpServletRequest) {
                String token = ((HttpServletRequest) param).getHeader("authorization");
                if (token.startsWith("Bearer ")) {
                    token = ((HttpServletRequest) param).getHeader("authorization").split(" ")[1]; // When the Token with Bearer.
                }
                Long id = JWT.decode(token).getClaim("id").asLong();
                if (id != null && id > 0) {
                    valid = true;
                }
            }
        }
        System.out.println("finished proxy");
        if (valid) {
            try {
                return originalMethod.proceed();
            } catch (RuntimeException e) {
                return ResponseEntity.status(401).body("Don't know you!");
            }
        } else {
            return ResponseEntity.status(401).body("Don't know you!");
        }
    }
}
