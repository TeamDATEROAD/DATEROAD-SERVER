package org.dateroad.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.jwt.JwtProvider;
import org.dateroad.auth.jwt.JwtValidator;
import org.dateroad.code.FailureCode;
import org.dateroad.common.Constants;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;
    private final JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = getAccessToken(request);
        final Long userId = jwtProvider.getSubject(accessToken);
        doAuthentication(userId);
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(final HttpServletRequest request) {
        final String accessToken = request.getHeader(Constants.AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(Constants.BEARER)) {
            return accessToken.substring(Constants.BEARER.length());
        }
        throw new UnauthorizedException(FailureCode.UNAUTHORIZED);
    }

    private void doAuthentication(Long userId) {
        UserAuthentication userAuthentication = UserAuthentication.createUserAuthentication(userId);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(userAuthentication);
    }
}
