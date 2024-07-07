package org.dateroad.auth.jwt;

import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;

public class JwtValidator {

    public void equalRefreshToken(String refreshToken, String storedRefreshToken) {
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new UnauthorizedException(FailureCode.UNAUTHORIZED);
        }
    }
}
