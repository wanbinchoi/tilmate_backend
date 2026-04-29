package com.tilmate.backend.member.service;

import com.tilmate.backend.config.JwtProvider;
import com.tilmate.backend.member.dto.LoginRequestDto;
import com.tilmate.backend.member.dto.LoginResponseDto;
import com.tilmate.backend.member.dto.SignupRequestDto;
import com.tilmate.backend.member.dto.SignupResponseDto;
import com.tilmate.backend.member.entity.Member;
import com.tilmate.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto dto){
        if(memberRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .build();

        Member saved = memberRepository.save(member);

        return SignupResponseDto.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .name(saved.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto dto){

        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        String token = jwtProvider.generateToken(member.getId(), member.getEmail());

        return LoginResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .accessToken(token)
                .build();

    }
}
