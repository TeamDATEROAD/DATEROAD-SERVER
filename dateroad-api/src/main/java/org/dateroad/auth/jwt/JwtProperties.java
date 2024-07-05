package org.dateroad.auth.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("jwt")
@Component
public class JwtProperties {
    private String secret;
    private long accessTokenExpireTime;
    private long refreshTokenExpireTime;
}


