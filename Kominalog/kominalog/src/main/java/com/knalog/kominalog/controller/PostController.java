package com.knalog.kominalog.controller;

import com.knalog.kominalog.domain.Post;
import com.knalog.kominalog.request.PostCreate;
import com.knalog.kominalog.response.PostResponse;
import com.knalog.kominalog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Controller 어노테이션 써도 되지만, 데이터 기반으로 API 를 생성하기 위해서 RestController 사용
// RestController 안쪽에 @Controller , @ResponseBody 가 있기때문에 좀더 쉽게 데이터를 받아올 수 있음

@Slf4j          // System.out.println(); 을 쓰지 않고 log 로 바로 param 을 확인
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    @PostMapping("/posts")
//    public Map<String, String> post(@RequestBody @Valid PostCreate request){ //, BindingResult result){
//    return Map.of(); 삭제
    public void post(@RequestBody @Valid PostCreate request){
//  ----------------------------------------------------------------------------------------------------------
//        이 코드의 문제점
//        1. 매번 메서드 마다 값을 검증 해야한다.
//              > 개발자가 까먹을 수 있다.
//              > 검증 부분에서 Bug가 발생할 수 있다.
//              > 코드가 길어져서 지저분해 보인다.
//        2. 응답값에 HashMap -> Map 이 아닌, 응답 클래스를 만들어 주는 것이 좋다.
//        3. 여러개의 에러 처리가 힘들다
//        4. 세 번이상의 반복적인 작업은 피해야 한다.

//        if (result.hasErrors()){
//            List<FieldError> fieldErrors =result.getFieldErrors();
//            FieldError firstFieldError = fieldErrors.get(0);
//            String fieldName = firstFieldError.getField();  // title
//            String errorMessage = firstFieldError.getDefaultMessage();  // ..에러 메시지
//
//            Map<String, String> error = new HashMap<>();
//            error.put(fieldName, errorMessage);
//            return error;
//        }
//  ----------------------------------------------------------------------------------------------------------

//  ----------------------------------------------------------------------------------------------------------
//        log.info("params={}", params.toString());

//        지금은 검증해야할 것이 2개이지만 양이 많아지면 문제가 되는점
//        1. 노가다
//        2. 무언가 3번이상 반복작업을 하게 된다면 내가 뭔가 잘못하고 있는건 아닐지 생각해보자.
//        3. 검증을 누락할 수 있다.
//        4. 생각보다 검증해야할 것이 많다.
//        5. 코드가 길어져서 지저분해 보인다.

//        String title = params.getTitle();
//        if(title == null || title.equals("")){
//            throw new Exception("타이틀 값이 없어요!");
//        }
//
//        String content = params.getContent();
//        if(content == null || content.equals("")){
//            // error
//        }
//  -----------------------------------------------------------------------------------------------------------
        // repository.save(params) 형식으로 바로 repository 로 저장하는 방법도 있으나 가급적 서비스 레이어를 만들어서
        // 서비스 레이어에서 repository 를 호출하는 방식으로 코딩하는것이 좋다.
        postService.write(request);
//        return Map.of();

    }
    /**
     * /post -> 글 전체 조회 (검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        PostResponse response = postService.get(id);
        return response;
    }

    // controller 는 service 를 호출, service 는 repository 를 호출해서 최종적으로 넘어온 json 값을 Post 라는 Entity 로 변환해서 저장하는 형식
}

// SSR -> jsp, thymeleaf, mustache, freemarker
//          -> html rendering ( SSR 의 경우는 html (서버) 에서 rendering 해주는 반면, )
// SPA -> vue
//          -> javascript + <-> API (JSON) ( 화면은 javascript 만들어 주고, 서버와의 통신은 API 로 함 JSON 형식으로 응답처리함 )
// <참고> vue -> vue + SSR = nuxt.js
// <참고> react -> react + SSR = next.js

// 테스트 파일 만드는 단축키 :
// 윈도우 => ctrl + shift + T
// Mac => Command + shift + T

// Http Method
// GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT 특징을 알아두자
// 글 등록시엔
// POST Method

// -----------------------------------------------------------------------------------------------------------

// 실제 코드에서 post test 하는 방법 첫번째
//public String post(@RequestParam String title, @RequestParam String content) {
//    log.info("title={}, content={}", title, content);
//    return "Hello World";
//}

// 실제 코드에서 post test 하는 방법 두번째
//public String post(@RequestParam Map<String, String> params) {
//    log.info("params={}", params);
//    String title = params.get("title"); <-- 이런식으로 params 를 받아 올 수 있다.
//    return "Hello World";
//}

// 실제 코드에서 post test 하는 방법 세번째
//public String post(@RequestParam Map<String, String> params) {
//    log.info("params={}", params);
//    return "Hello World";
//}

// ------------------------------------------------------------------------------------------------------------

// 데이터 검증의 이유
// 1. Client 개발자가 깜박할 수 있다. 실수로 값을 안보낼 수 있다.
// 2. Client Bug 로 값이 누락될 수 있다.
// 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
// 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
// 5. 서버 개발자의 편안함을 위해서