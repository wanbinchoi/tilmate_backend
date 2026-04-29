package com.tilmate.backend.member.controller;

import com.tilmate.backend.member.dto.LoginRequestDto;
import com.tilmate.backend.member.dto.LoginResponseDto;
import com.tilmate.backend.member.dto.SignupRequestDto;
import com.tilmate.backend.member.dto.SignupResponseDto;
import com.tilmate.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto dto){
        return ResponseEntity.ok(memberService.signup(dto));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(memberService.login(dto));
    }
}
