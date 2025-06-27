package com.tikitaka.api.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.jwt.JwtTokenProvider;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler  implements AuthenticationSuccessHandler {

    private final String redirectUrl = "http://localhost:5173/oauth/callback";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        String token = jwtTokenProvider.createToken(user.getSub(), user.getRole());

        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                                                .httpOnly(true)
                                                .secure(false)
                                                .sameSite("Lax")
                                                .path("/")
                                                .maxAge(Duration.ofDays(1))
                                                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        response.sendRedirect(redirectUrl
                            + "?sub=" + user.getSub()
                            + "&token=" + token
                            + "&message=success");
    }
}
