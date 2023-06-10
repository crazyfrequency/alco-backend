package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Comment;
import com.alco.algorithmic.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.post.id = :post and c.id < :offset order by c.id desc limit 100")
    List<Comment> findFirst100CommentsByPostIdAfterOrderById(Long post, Long offset);

    @Query("select c from Comment c where c.post.id = :post order by c.id desc limit 100")
    List<Comment> findFirst100CommentsByPostIdOrderById(Long post);

    @Transactional
    int deleteByIdAndAuthor(Long id, Account author);

}
