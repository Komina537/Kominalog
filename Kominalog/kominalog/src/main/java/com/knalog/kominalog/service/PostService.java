package com.knalog.kominalog.service;

import com.knalog.kominalog.domain.Post;
import com.knalog.kominalog.repository.PostRepository;
import com.knalog.kominalog.request.PostCreate;
import com.knalog.kominalog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        // postCreate -> Entity (일반 클래스를 엔티티 형태로 변환해줘야함)

        // Post post = new Post(postCreate.getTitle(), postCreate.getContent());

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }

    public PostResponse get(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 글입니다."));

        PostResponse response  = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return response;
    }
}
