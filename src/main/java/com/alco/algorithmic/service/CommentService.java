package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.CommentRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Comment;
import com.alco.algorithmic.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Long addCommentByPostId(Long post, Long author, String text) {
        Comment comment = Comment.builder()
                .text(text).post(
                        Post.builder().id(post).build()
                ).author(
                        Account.builder().id(author).build()
                ).build();
        return commentRepository.save(comment).getId();
    }

    public List<Comment> getCommentsByPostId(Long id, Long offset) {
        if (offset == null)
            return commentRepository.findFirst100CommentsByPostIdOrderById(id);
        return commentRepository.findFirst100CommentsByPostIdAfterOrderById(id, offset);
    }

    public boolean deleteCommentById(Long comment, Long author) {
        return commentRepository.deleteByIdAndAuthor(
                comment,
                Account.builder().id(author).build()
        ) != 0;
    }

}
