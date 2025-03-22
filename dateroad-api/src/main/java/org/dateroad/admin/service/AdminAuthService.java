package org.dateroad.admin.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.admin.domain.Admin;
import org.dateroad.admin.dto.AdminCreateDto;
import org.dateroad.admin.dto.AdminLoginReq;
import org.dateroad.admin.dto.AdminLoginRes;
import org.dateroad.admin.repository.AdminRepository;
import org.dateroad.auth.jwt.JwtProvider;
import org.dateroad.auth.jwt.Token;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAuthService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AdminLoginRes login(AdminLoginReq req) {
        Admin admin = adminRepository.findByUsername(req.username())
                .orElseThrow(() -> new UnauthorizedException(FailureCode.INVALID_ACCESS_TOKEN_VALUE));

        if (!admin.matchPassword(req.password(), passwordEncoder)) {
            throw new UnauthorizedException(FailureCode.INVALID_ACCESS_TOKEN_VALUE);
        }

        Token responseToken = jwtProvider.issueToken(admin.getId());
        return AdminLoginRes.of(responseToken, admin.getUsername());
    }

    @Transactional
    public void createAdmin(AdminCreateDto dto) {
        boolean check = adminRepository.existsByUsername(dto.username());
        if (check) {
            throw new IllegalArgumentException("이미 존재하는 관리자 계정입니다.");
        }
        Admin admin = Admin.create(dto.username(), dto.password() , passwordEncoder);
        adminRepository.save(admin);
    }
} 