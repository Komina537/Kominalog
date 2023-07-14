package com.knalog.kominalog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String title;

    @Lob    // Long-Text 형태로 DB에 저장
    private  String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 예를 들어 Client 측에서 title 길이를 10이하로 요청했을경우
    public String getTitle(){
        // 서비스의 정책을 넣어두면 안됨
        // return this.title.substring(0,10);
        return this.title;
    }
    // 문제점 : Post 에서 이것을 수정 했을 경우, 나중에 특정 기능이 추가됬을때 저 코드로 인해서 정책이 충돌할 수 있음
}
