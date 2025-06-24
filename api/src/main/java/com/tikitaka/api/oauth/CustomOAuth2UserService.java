package com.tikitaka.api.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.exception.NeedsVerificationException;
import com.tikitaka.api.repository.UserRepository;
import com.tikitaka.api.util.VerificationService;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

     private final UserRepository userRepository;
     private final VerificationService verificationService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(request);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String sub = (String) attributes.get("sub");
        String name = (String) attributes.get("name");

        System.out.println("허용되지 않는 이메일 도메인: " + email);
        if (!email.endsWith("@dankook.ac.kr")) {
            throw new OAuth2AuthenticationException("해당 도메인은 허용되지 않습니다.");
        }

        Optional<User> optionalUser = userRepository.findBySub(sub);

        System.out.println("허용되지 않는 이메일 도메인: " + email);
        if (optionalUser.isPresent()) {
            return new CustomOAuth2User(optionalUser.get(), attributes);
        }

         // 사용자 인증되지 않았으면 이메일 전송 후 예외
        verificationService.saveVerificationData(email, sub, name);
        System.out.println("허용되지 않는 이메일 도메인: " + email);

        throw new NeedsVerificationException(sub);
    }
}
