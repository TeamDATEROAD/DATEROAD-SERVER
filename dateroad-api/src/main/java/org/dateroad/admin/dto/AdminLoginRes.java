package org.dateroad.admin.dto;

import org.dateroad.auth.jwt.Token;

public record AdminLoginRes(
    Token token,
    String name
) {
    public static AdminLoginRes of(Token token, String name) {
        return new AdminLoginRes(token, name);
    }
} 