package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.Post;
import com.alco.algorithmic.enums.ReactType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostResponse {

    private Long id;

    private AccountResponse author;

    private String title;

    private String text;

    private List<FileResponse> files;

    private Long likes;

    private Long dislikes;

    private Date createdAt;

    private ReactType react;

    public PostResponse(Post post, Long likes, Long dislikes, ReactType react) {
        this.id = post.getId();
        this.author = new AccountResponse(post.getAuthor());
        this.title = post.getTitle();
        this.text = post.getText();
        this.files = post.getFiles() != null ? post.getFiles().stream().map(FileResponse::new).toList() : null;
        this.createdAt = post.getCreatedAt();
        this.likes = likes;
        this.dislikes = dislikes;
        this.react = react;
    }
}
