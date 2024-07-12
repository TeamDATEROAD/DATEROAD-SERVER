package org.dateroad.user.dto.request;

import org.dateroad.tag.domain.DateTagType;
import org.dateroad.user.domain.Platform;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UserSignUpReq(
        String name,
        Platform platform
) {
}
