package com.tilmate.backend.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private LocalDateTime createdAt;

    // 나중에 Builder 쓸 때 굳이 값 안넣어줘도
    // repository.save(만든거) 하게 되면
    // 이 메소드가 알아서 실행되게 되서
    // 나중에 딴데서 또 builder를 만들 떄
    // 코드가 한줄 줄어듬
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

}
