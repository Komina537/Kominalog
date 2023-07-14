package com.knalog.kominalog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.security.PrivateKey;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")   // 빈값일때 자동으로 검증해주는 어노테이션 , 2.3 버전 이후에는 직접 의존성을 추가해주어야함
    // build.gradle --> implementation 'org.springframework.boot:spring-boot-starter-validation'
    // PostController.java --> post 인자값에 @Valid 추가
    private String content;

    // PostCreate 를 생성자 클래스 작성
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 빌더의 장점
    //      - 가독성이 좋아진다.
    //      - 값 생성에 대한 유연함
    //      - 필요한 값만 받을 수 있다. --> (오버로딩 가능한 조건 찾아보자)
    //      - 객체의 불변성
}

// toString() 함수 override 하는 단축키 (Generate) --> Alt + insert

// @Setter 를 사용하면 아래 코드를 사용하지 않아도 됨

//public void setTitle(String title){
//    this.title = title;
//}
//public void setContent(String content){
//    this.content = content;
//}

// PostController 에서 log 를 확인하기 위해서 override 했던것임. @toString 어노테이션으로 대체함
//@Override
//public String toString() {
//    return "PostCreate{" +
//            "title='" + title + '\'' +
//            ", content='" + content + '\'' +
//            '}';
//}