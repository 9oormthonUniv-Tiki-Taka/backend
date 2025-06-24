package com.tikitaka.api.util;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    //test용
    public User getCurrentUser() {
        String email;
        try {
            email = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            email = null;
        }

        final String finalEmail = (email == null || email.equals("anonymousUser")) ? "test@tikitaka.com" : email;

        return userRepository.findByEmail(finalEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + finalEmail));
    }
    /*
    배포용
    public User getCurrentUser() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    if (email == null || email.equals("anonymousUser")) {
        throw new RuntimeException("Authentication required");
        // 또는 직접 인증 예외를 던질 수도 있어요
        // throw new UsernameNotFoundException("Authentication required");
    }

    return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
}
     */

}

