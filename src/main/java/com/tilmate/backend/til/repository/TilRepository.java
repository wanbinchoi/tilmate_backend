package com.tilmate.backend.til.repository;

import com.tilmate.backend.til.entity.Til;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 기본적인 CRUD(글 쓰기, 글 읽기, 글 수정, 글 삭제)
 * 기본적인 JpaRepository의 메소들로는 id기반의 조회밖에 못함
 * 특정 멤버의 TIL 조회 같은 경우는 직접 만들어야함
 */
public interface TilRepository extends JpaRepository<Til, Long> {
    // 특정 멤버의 Til들만 조회하는 메소드
    List<Til> findByMemberId(Long memberId);
}