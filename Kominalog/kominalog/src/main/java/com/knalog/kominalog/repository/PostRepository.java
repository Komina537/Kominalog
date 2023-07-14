package com.knalog.kominalog.repository;

import com.knalog.kominalog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
