package com.tikitaka.api.util;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.oauth.CustomOAuth2User;
import com.tikitaka.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Authentication required");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomOAuth2User customUser) {
            return customUser.getUser();
        }

        if (principal instanceof OAuth2User oAuth2User) {
            String sub = oAuth2User.getAttribute("sub");
            if (sub != null && !sub.isEmpty()) {
                return userRepository.findBySub(sub)
                        .orElseThrow(() -> new RuntimeException("User not found with sub: " + sub));
            }

            throw new RuntimeException("Sub not found in OAuth2 attributes");
        }

        if (principal instanceof com.tikitaka.api.jwt.CustomUserDetails userDetails) {
            String sub = userDetails.getUsername();
            return userRepository.findBySub(sub)
                    .orElseThrow(() -> new RuntimeException("User not found with sub: " + sub));
        }

        throw new RuntimeException("Unsupported principal type: " + principal.getClass().getName());
    }

}
