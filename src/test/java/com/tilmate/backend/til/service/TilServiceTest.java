package com.tilmate.backend.til.service;

import com.tilmate.backend.member.entity.Member;
import com.tilmate.backend.member.repository.MemberRepository;
import com.tilmate.backend.til.dto.request.TilUpdateRequestDto;
import com.tilmate.backend.til.dto.request.TilWriteRequestDto;
import com.tilmate.backend.til.dto.TilResponseDto;
import com.tilmate.backend.til.entity.Til;
import com.tilmate.backend.til.repository.TilRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TilServiceTest {

    @Mock
    private TilRepository tilRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TilService tilService;

    // ======================== write ========================

    @Test
    @DisplayName("TIL 작성 성공")
    void write_success() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .name("테스터")
                .build();

        TilWriteRequestDto dto = new TilWriteRequestDto("오늘 배운 것", "JPA 공부했다");

        Til savedTil = Til.builder()
                .id(1L)
                .member(member)
                .title("오늘 배운 것")
                .content("JPA 공부했다")
                .build();

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(tilRepository.save(any(Til.class))).willReturn(savedTil);

        // when
        TilResponseDto result = tilService.write(1L, dto);

        // then
        assertThat(result.getTitle()).isEqualTo("오늘 배운 것");
        assertThat(result.getContent()).isEqualTo("JPA 공부했다");
        assertThat(result.getMemberId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("TIL 작성 실패 - 존재하지 않는 회원")
    void write_fail_memberNotFound() {
        // given
        TilWriteRequestDto dto = new TilWriteRequestDto("오늘 배운 것", "JPA 공부했다");
        given(memberRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tilService.write(999L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    // ======================== findById ========================

    @Test
    @DisplayName("TIL 단건 조회 성공")
    void findById_success() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .name("테스터")
                .build();

        Til til = Til.builder()
                .id(1L)
                .member(member)
                .title("오늘 배운 것")
                .content("JPA 공부했다")
                .build();

        given(tilRepository.findById(1L)).willReturn(Optional.of(til));

        // when
        TilResponseDto result = tilService.findById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("오늘 배운 것");
    }

    @Test
    @DisplayName("TIL 단건 조회 실패 - 존재하지 않는 TIL")
    void findById_fail_tilNotFound() {
        // given
        given(tilRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tilService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 TIL입니다.");
    }

    // ======================== findAllByMember ========================

    @Test
    @DisplayName("내 TIL 목록 조회 성공")
    void findAllByMember_success() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .name("테스터")
                .build();

        List<Til> tils = List.of(
                Til.builder().id(1L).member(member).title("TIL 1").content("내용 1").build(),
                Til.builder().id(2L).member(member).title("TIL 2").content("내용 2").build()
        );

        given(tilRepository.findByMemberId(1L)).willReturn(tils);

        // when
        List<TilResponseDto> result = tilService.findAllByMember(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("TIL 1");
        assertThat(result.get(1).getTitle()).isEqualTo("TIL 2");
    }

    // ======================== update ========================

    @Test
    @DisplayName("TIL 수정 성공")
    void update_success() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .name("테스터")
                .build();

        Til til = Til.builder()
                .id(1L)
                .member(member)
                .title("원래 제목")
                .content("원래 내용")
                .build();

        TilUpdateRequestDto dto = new TilUpdateRequestDto("수정된 제목", "수정된 내용");

        given(tilRepository.findById(1L)).willReturn(Optional.of(til));

        // when
        TilResponseDto result = tilService.update(1L, 1L, dto);

        // then
        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(result.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    @DisplayName("TIL 수정 실패 - 존재하지 않는 TIL")
    void update_fail_tilNotFound() {
        // given
        TilUpdateRequestDto dto = new TilUpdateRequestDto("수정된 제목", "수정된 내용");
        given(tilRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tilService.update(1L, 999L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 TIL입니다.");
    }

    @Test
    @DisplayName("TIL 수정 실패 - 권한 없음")
    void update_fail_unauthorized() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .name("테스터")
                .build();

        Til til = Til.builder()
                .id(1L)
                .member(member)
                .title("원래 제목")
                .content("원래 내용")
                .build();

        TilUpdateRequestDto dto = new TilUpdateRequestDto("수정된 제목", "수정된 내용");
        given(tilRepository.findById(1L)).willReturn(Optional.of(til));

        // when & then — memberId 2L (다른 사람) 로 수정 시도
        assertThatThrownBy(() -> tilService.update(2L, 1L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수정 권한이 없습니다.");
    }

    // ======================== delete ========================

    @Test
    @DisplayName("TIL 삭제 성공")
    void delete_success() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .name("테스터")
                .build();

        Til til = Til.builder()
                .id(1L)
                .member(member)
                .title("오늘 배운 것")
                .content("JPA 공부했다")
                .build();

        given(tilRepository.findById(1L)).willReturn(Optional.of(til));

        // when
        tilService.delete(1L, 1L);

        // then — deleteById가 실제로 호출됐는지 검증
        verify(tilRepository).deleteById(1L);
    }

    @Test
    @DisplayName("TIL 삭제 실패 - 존재하지 않는 TIL")
    void delete_fail_tilNotFound() {
        // given
        given(tilRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tilService.delete(1L, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 TIL입니다.");
    }

    @Test
    @DisplayName("TIL 삭제 실패 - 권한 없음")
    void delete_fail_unauthorized() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("test@test.com")
                .password("password")
                .name("테스터")
                .build();

        Til til = Til.builder()
                .id(1L)
                .member(member)
                .title("오늘 배운 것")
                .content("JPA 공부했다")
                .build();

        given(tilRepository.findById(1L)).willReturn(Optional.of(til));

        // when & then — memberId 2L (다른 사람) 로 삭제 시도
        assertThatThrownBy(() -> tilService.delete(2L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제 권한이 없습니다.");
    }
}