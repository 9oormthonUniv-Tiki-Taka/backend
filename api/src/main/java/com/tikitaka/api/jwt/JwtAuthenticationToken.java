package com.tikitaka.api.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // 비밀번호 없음
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

