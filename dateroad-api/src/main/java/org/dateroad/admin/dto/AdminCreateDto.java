package org.dateroad.admin.dto;

public record AdminCreateDto(
        String username,
        String password,
        String lat
) {
}
