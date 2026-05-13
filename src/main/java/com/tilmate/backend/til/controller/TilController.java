package com.tilmate.backend.til.controller;

import com.tilmate.backend.member.service.MemberService;
import com.tilmate.backend.til.dto.TilResponseDto;
import com.tilmate.backend.til.dto.request.TilUpdateRequestDto;
import com.tilmate.backend.til.dto.request.TilWriteRequestDto;
import com.tilmate.backend.til.service.TilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/til")
@RequiredArgsConstructor
public class TilController {

    private final TilService tilService;
    private final MemberService memberService;

    // Til 작성
    @PostMapping
    public ResponseEntity<TilResponseDto> write(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody TilWriteRequestDto dto){
        return ResponseEntity.ok(tilService.write(memberId, dto));
    }

    // 전체 Til 최신순 조회
    @GetMapping
    public ResponseEntity<List<TilResponseDto>> getAll() {
        return ResponseEntity.ok(tilService.findAll());
    }

    // Til 단건 조회
    @GetMapping("/{tilId}")
    public ResponseEntity<TilResponseDto> getOne(
            @PathVariable Long tilId){
        return ResponseEntity.ok(tilService.findById(tilId));
    }

    // 내 Til 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<TilResponseDto>> getMyTils(
            @AuthenticationPrincipal Long memberId
    ){
        return ResponseEntity.ok(tilService.findAllByMember(memberId));
    }

    // Til 수정
    @PutMapping("/{tilId}")
    public ResponseEntity<TilResponseDto> update(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long tilId,
            @Valid @RequestBody TilUpdateRequestDto dto
            ){
        return ResponseEntity.ok(tilService.update(memberId, tilId, dto));
    }

    // til 삭제
    @DeleteMapping("/{tilId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Long memberID,
            @PathVariable Long tilId
    ){
        tilService.delete(memberID, tilId);
        return ResponseEntity.noContent().build();
    }

}
