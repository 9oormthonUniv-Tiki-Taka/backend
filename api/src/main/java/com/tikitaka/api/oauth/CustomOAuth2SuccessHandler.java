package com.tikitaka.api.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.jwt.JwtTokenProvider;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler  implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        String token = jwtTokenProvider.createToken(user.getSub(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("token", token);
        result.put("user", Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "role", user.getRole().name(),
            "avatarURL", "temp",
            "name", user.getName(),
            "sub", user.getSub()
        ));

        new ObjectMapper().writeValue(response.getWriter(), result);
    }
}
