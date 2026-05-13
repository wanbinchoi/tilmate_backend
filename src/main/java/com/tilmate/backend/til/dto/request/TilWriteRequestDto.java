package com.tilmate.backend.til.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TilWriteRequestDto {

    @NotBlank(message="제목은 빈칸일 수 없습니다.")
    private String title;

    @NotBlank(message="내용을 입력해주세요.")
    private String content;
}
