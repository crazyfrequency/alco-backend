package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Post;
import com.alco.algorithmic.entity.React;
import com.alco.algorithmic.enums.ReactType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReactRepository extends JpaRepository<React, Long> {

    @Query("select count(r) from React r where r.post.id = :id and r.type = com.alco.algorithmic.enums.ReactType.LIKE")
    public Long countLikesAllByPostId(Long id);

    public Long countAllByPostAndType(Post author, ReactType type);

    public React getReactByAuthorAndPost(Account author, Post post);

    @Transactional
    public void deleteByAuthorAndPost(Account author, Post post);

}
