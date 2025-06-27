package com.tikitaka.api.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.tikitaka.api.exception.NeedsVerificationException;

import java.io.IOException;

@Component
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String redirectUrl = "http://localhost:5173/oauth/callback";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (exception instanceof NeedsVerificationException) {
            String sub = ((NeedsVerificationException) exception).getSub();
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(redirectUrl 
                                + "?sub=" + sub
                                + "&message=need_verification");
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect(redirectUrl 
                                + "?message=failure");
        }
    }
}