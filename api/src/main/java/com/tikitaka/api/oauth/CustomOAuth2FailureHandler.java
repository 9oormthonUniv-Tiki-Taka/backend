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

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        if (exception instanceof NeedsVerificationException) {
            String sub = ((NeedsVerificationException) exception).getSub();
            getRedirectStrategy().sendRedirect(request, response, "/verify?sub=" + sub);
        }
        else {
            getRedirectStrategy().sendRedirect(request, response, "/login?error");
        }
    }
}