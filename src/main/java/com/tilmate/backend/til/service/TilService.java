package com.tilmate.backend.til.service;

import com.tilmate.backend.member.repository.MemberRepository;
import com.tilmate.backend.til.dto.request.TilWriteRequestDto;
import com.tilmate.backend.til.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TilService {

    private final TilRepository tilRepository;
    private final MemberRepository memberRepository;

    // Til 작성하기
    public void writeTil(Long memberId, TilWriteRequestDto tilWriteRequestDto) {

    }
}