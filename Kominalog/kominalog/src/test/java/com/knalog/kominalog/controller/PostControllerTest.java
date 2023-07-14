package com.knalog.kominalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knalog.kominalog.domain.Post;
import com.knalog.kominalog.repository.PostRepository;
import com.knalog.kominalog.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest // test 시 MockMvc 가 Null 이기 때문에 이 어노테이션을 통해서 주입
            // 간단한 컨트롤러의 Web 레이어 테스트에는 괜찮은데 , 애플리케이션의 전반적인 테스트를 위해서는 @SpringBootTest 를 사용해야함
            // 문제는 이 어노테이션을 사용하면 @WebMvcTest 를 사용할 수 없으므로 mockMvc 에대한 bean 이 주입이 안되므로, @AutoConfigureMockMvc 를 사용하면 된다.
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach // test 메소드들이 각각 실행되기 전에 항상 수행이 되도록 보장해주는 메소드
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World 를 출력한다.")
    void test() throws Exception {
        // 글 제목
        // 글 내용

        // given
        // @Builder 어노테이션 없이 할때
        // PostCreate request = new PostCreate("제목입니다.","내용입니다.");

        PostCreate request =PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // ObjectMapper 는 실무에서 많이 쓰이는 메소드이므로 꼭 따로 알아두자.
        // ObjectMapper 새로생성하는 방법도 있지만 Spring-boot 에서 @Autowired 어노테이션으로 기본적으로 ObjectMapper 의 Bean 을 제공한다.
        // ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);

        // expected
        // Content type -> application/json 형식으로 보낸다.
        //param 은 둘다 string 형태
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());    // http 요청에 대한 summary 를 얻고 싶을때 사용
    }

    @Test
    @DisplayName("/posts 요청시 title 값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate request =PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        // title 값을 공백으로 보냈을 경우
                        //.content("{\"title\": \"\", \"content\": \"내용입니다\"}")
                        // title 값을 null 로 보냈을 경우 --> 결과 : @NotBlank 가 null 값도 자동으로 검증해준다.
                        .content(json)
                )
                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Hello World"))
                // jsonPath 에 대한 검증 방법은 따로 찾아서 추가로 공부가 필요함..
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());    // http 요청에 대한 summary 를 얻고 싶을때 사용
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // given
        PostCreate request =PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
     void test4() throws Exception{
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
        // then
    }
}


// HTTP 참고 사이트 : https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/POST
// Content Type 이란? -> 서버로 요청을 할때나 혹은 요청을 받을때 http 의 헤더값
// 예전에는 application/x-www-form-urlencoded 를 많이 썼었음.