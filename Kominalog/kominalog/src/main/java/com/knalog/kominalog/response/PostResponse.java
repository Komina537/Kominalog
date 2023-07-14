package com.knalog.kominalog.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 서비스 정책에 맞는 클래스 생성
 */
@Getter
@Builder
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0,10);
        this.content = content;
    }
}
