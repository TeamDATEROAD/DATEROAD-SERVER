package org.dateroad.users.dto.request;

import org.dateroad.user.domain.Platform;

public record UserSignInReq(
        Platform platform
) {
}
