package org.dateroad.admin.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.dateroad.user.domain.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminUserResponse {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String profileImage;
    private int warningCount;
    private LocalDateTime createdAt;

    public static AdminUserResponse from(User user) {
        return AdminUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .profileImage(user.getImageUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }
} 