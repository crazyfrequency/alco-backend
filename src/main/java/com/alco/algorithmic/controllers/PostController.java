package com.alco.algorithmic.controllers;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Post;
import com.alco.algorithmic.enums.ReactType;
import com.alco.algorithmic.responseRequests.PostRequest;
import com.alco.algorithmic.responseRequests.PostResponse;
import com.alco.algorithmic.service.PostService;
import com.alco.algorithmic.service.ReactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/posts")
@Validated
public class PostController {
    private final PostService postService;
    private final ReactService reactService;

    private PostResponse getPostResponse(Post post, Account user) {
        Long likes = reactService.getPostLikes(post.getId());
        Long dislikes = reactService.getPostDislikes(post.getId());
        ReactType react = reactService.getReactByUserAndPostId(user, post.getId());
        return new PostResponse(post, likes, dislikes, react);
    }

    @GetMapping("/my")
    public List<PostResponse> getMyPosts(@RequestParam(name = "offset", required = false) Long offset) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return postService.getPostsByAccountId(account.getId(), offset).stream().map(post -> getPostResponse(post, account)).toList();
    }

    @GetMapping("/lent")
    public List<PostResponse> getMyLent(@RequestParam(name = "offset", required = false) Long offset) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return postService.getFriendsPostsByAccountId(account.getId(), offset).stream().map(post -> getPostResponse(post, account)).toList();
    }

    @GetMapping("/{id}")
    public List<PostResponse> getUserPosts(@PathVariable Long id, @RequestParam(name = "offset", required = false) Long offset) {
        var account = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return postService.getPostsByAccountId(id, offset).stream().map(post -> getPostResponse(
                post,
                account instanceof Account ? (Account) account : null
        )).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePostById(id);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/{id}/data")
    public PostResponse getPostById(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getPostResponse(postService.getPostById(id), account);
    }

    @GetMapping("/{id}/like")
    public Long getLike(@PathVariable Long id) {
        return reactService.getPostLikes(id);
    }

    @PostMapping("/{id}/like")
    public String setLike(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reactService.addLike(account.getId(), id);
        return "ok";
    }

    @GetMapping("/{id}/dislike")
    public Long getDislike(@PathVariable Long id) {
        return reactService.getPostDislikes(id);
    }

    @PostMapping("/{id}/dislike")
    public String setDislike(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reactService.addDislike(account.getId(), id);
        return "ok";
    }

    @PostMapping("/{id}/none")
    public String deleteLikeOrDislike(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reactService.deleteByAccountIdAndPostId(account.getId(), id);
        return "ok";
    }

    @PostMapping()
    public Long addPost(@Valid @RequestBody PostRequest post) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return postService.addPostByAccountId(account.getId(), post).getId();
    }

}
