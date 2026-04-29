package com.tilmate.backend.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseDto {
    private Long id;;
    private String email;
    private String name;
}
