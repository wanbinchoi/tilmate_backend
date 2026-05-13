package com.tilmate.backend.til.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Builder패턴을 사용하는 이유는
 * 나중에 security principal로 member id값을 받아서 사용해야 하기 때문에
 */
@Getter
@Builder
public class TilResponseDto {
    private Long id;
    private Long memberId;
    private String authorName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
