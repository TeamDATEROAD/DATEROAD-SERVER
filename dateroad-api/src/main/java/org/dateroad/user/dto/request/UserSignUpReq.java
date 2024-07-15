package org.dateroad.user.dto.request;

import org.dateroad.user.domain.Platform;


public record UserSignUpReq(
        String name,
        Platform platform
) {
}
