package com.alco.algorithmic.controllers;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.responseRequests.CommentRequest;
import com.alco.algorithmic.responseRequests.CommentResponse;
import com.alco.algorithmic.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/comments")
@Validated
public class CommentController {
    @Autowired
    public CommentService commentService;

    @GetMapping("/{id}")
    public List<CommentResponse> getComments(@PathVariable Long id, @RequestParam(name = "offset", required = false) Long offset) {
        return commentService.getCommentsByPostId(id, offset).stream().map(CommentResponse::new).toList();
    }

    @PutMapping("/{id}")
    public Long addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest comment) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return commentService.addCommentByPostId(id, account.getId(), comment.getText());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         boolean res = commentService.deleteCommentById(id, account.getId());
         return res ? new ResponseEntity<>("OK", HttpStatus.OK) : new ResponseEntity<>("Nothing removed", HttpStatus.BAD_REQUEST);
    }
}
