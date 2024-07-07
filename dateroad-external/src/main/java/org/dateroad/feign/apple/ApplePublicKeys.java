package org.dateroad.feign.apple;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApplePublicKeys {
    private List<ApplePublicKey> keys;

    // alg(알고리즘) 및 kid(키 식별자) 값을 사용하여 일치하는 공개 키 찾기
    public ApplePublicKey getMatchesKey(final String alg, final String kid) {
        return keys.stream()
                .filter(applePublicKey -> applePublicKey.alg().equals(alg) && applePublicKey.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException(FailureCode.INVALID_APPLE_IDENTITY_TOKEN));
    }
}
