package com.tikitaka.api.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.tikitaka.api.exception.NeedsVerificationException;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        if (exception instanceof NeedsVerificationException) {
            String sub = ((NeedsVerificationException) exception).getSub();        
            result.put("status", "NEEDS_VERIFICATION");
            result.put("sub", sub);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            result.put("status", "ERROR");
            result.put("message", "인증에 실패했습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        }

        new ObjectMapper().writeValue(response.getWriter(), result);
    }
}