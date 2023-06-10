package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.Comment;
import lombok.Data;

import java.util.Date;

@Data
public class CommentResponse {

    private Long id;

    private AccountResponse author;

    private Long post;

    private String text;

    private Date createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.author = new AccountResponse(comment.getAuthor());
        this.post = comment.getPost().getId();
        this.text = comment.getText();
        this.createdAt = comment.getCreatedAt();
    }

}
