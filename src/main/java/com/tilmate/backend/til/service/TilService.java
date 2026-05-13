package com.tilmate.backend.til.service;

import com.tilmate.backend.member.entity.Member;
import com.tilmate.backend.member.repository.MemberRepository;
import com.tilmate.backend.til.dto.TilResponseDto;
import com.tilmate.backend.til.dto.request.TilUpdateRequestDto;
import com.tilmate.backend.til.dto.request.TilWriteRequestDto;
import com.tilmate.backend.til.entity.Til;
import com.tilmate.backend.til.repository.TilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TilService {

    private final TilRepository tilRepository;
    private final MemberRepository memberRepository;

    // til 작성하기
    @Transactional
    public TilResponseDto write(Long memberId, TilWriteRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Til til = Til.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        //builder로 만든거 저장하고 저장된거 반환
        Til saved = tilRepository.save(til);

        return toDto(saved);
    }

    // til 조회
    @Transactional(readOnly = true)
    public TilResponseDto findById(Long tilId) {
        Til til = tilRepository.findById(tilId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 TIL입니다."));
        return toDto(til);
    }

    // 전체 til 최신순 조회
    @Transactional(readOnly = true)
    public List<TilResponseDto> findAll() {
        return tilRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // 특정 회원의 til
    @Transactional(readOnly = true)
    public List<TilResponseDto> findAllByMember(Long memberID){
        return tilRepository.findByMemberId(memberID)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // til 수정
    @Transactional
    public TilResponseDto update(Long memberId, Long tilId, TilUpdateRequestDto dto) {
        Til til = tilRepository.findById(tilId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 TIL입니다."));

        if(!til.getMember().getId().equals(memberId)){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        til.update(dto.getTitle(), dto.getContent());

        return toDto(til);
    }

    // til 삭제
    @Transactional
    public void delete(Long memberId, Long tilId) {
        Til til = tilRepository.findById(tilId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 TIL입니다."));

        if (!til.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        tilRepository.deleteById(tilId);
    }

    private TilResponseDto toDto(Til til) {
        return TilResponseDto.builder()
                .id(til.getId())
                .memberId(til.getMember().getId())
                .authorName(til.getMember().getName())
                .title(til.getTitle())
                .content(til.getContent())
                .createdAt(til.getCreatedAt())
                .updatedAt(til.getUpdatedAt())
                .build();
    }
}