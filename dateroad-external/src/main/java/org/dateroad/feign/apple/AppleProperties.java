package org.dateroad.feign.apple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("feign.apple")
@Component
public class AppleProperties {
    private String iss;
    private String aud;
    private String clientId;
    private String kid;
    private String alg;
    private String type;
}
