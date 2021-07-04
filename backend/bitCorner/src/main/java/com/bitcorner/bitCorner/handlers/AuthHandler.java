package com.bitcorner.bitCorner.handlers;

import com.bitcorner.bitCorner.utility.JWTBuilder;
import com.kastkode.springsandwich.filter.api.BeforeHandler;
import com.kastkode.springsandwich.filter.api.Flow;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

//import javax.servlet.http.HttpServletRequest;
import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthHandler implements BeforeHandler {

    Logger logger = LoggerFactory.getLogger(AuthHandler.class);

    @Autowired
    JWTBuilder jwtbuilder;


    @Override
    public Flow handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, String[] flags) throws Exception {
        logger.debug(request.getMethod() + "request is executing on" + request.getRequestURI());
        String token = request.getHeader("Authorization");

        if( token == null) {
            System.out.println("No JWT token!!!");
            throw new AuthException("Auth token is required");
        }

        System.out.println("Found JWT token.");
        JwtClaims claims = jwtbuilder.generateParseToken(token);
        request.setAttribute("username", claims.getClaimValue("username").toString());
        // do any db check if needed
        return Flow.CONTINUE;
    }
}