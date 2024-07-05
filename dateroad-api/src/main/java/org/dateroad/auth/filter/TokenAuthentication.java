package org.dateroad.auth.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class TokenAuthentication implements Authentication {
    private final String token;
    private final Long userId;
    private boolean isAuthenticated = true;

    @Override
    public String getName() {
        return userId.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Long getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public static TokenAuthentication createTokenAuthentication(String token, Long userId) {
        return new TokenAuthentication(token, userId);
    }
}