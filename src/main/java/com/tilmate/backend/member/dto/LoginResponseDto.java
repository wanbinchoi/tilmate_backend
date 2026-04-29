package com.tilmate.backend.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private Long id;
    private String email;
    private String name;
    private String accessToken;
}
