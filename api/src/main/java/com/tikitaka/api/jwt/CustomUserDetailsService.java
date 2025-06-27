package com.tikitaka.api.jwt;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 생성자 주입
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 이메일로 유저를 찾아서 UserDetails 형태로 반환
    public UserDetails loadUserByUsername(String sub) throws UsernameNotFoundException {
        User user = userRepository.findBySub(sub)
                .orElseThrow(() -> new UsernameNotFoundException("해당 Sub의 유저를 찾을 수 없습니다: " + sub));

        return new CustomUserDetails(user);
    }

    public UserDetails createUserDetails(User user) {
        return new CustomUserDetails(user);
    }
}