package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.ReactRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Post;
import com.alco.algorithmic.entity.React;
import com.alco.algorithmic.enums.ReactType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReactService {

    @Autowired
    private ReactRepository reactRepository;

    @Transactional
    public Long addLike(Long authorId, Long postId) {
        Account account = Account.builder().id(authorId).build();
        Post post = Post.builder().id(postId).build();
        React react = reactRepository.getReactByAuthorAndPost(account, post);
        if(react!=null) react.setType(ReactType.LIKE);
        return react==null?
                reactRepository.save(React.builder().author(account).post(post).type(ReactType.LIKE).build()).getId():
                reactRepository.save(react).getId();
    }

    @Transactional
    public Long addDislike(Long authorId, Long postId) {
        Account account = Account.builder().id(authorId).build();
        Post post = Post.builder().id(postId).build();
        React react = reactRepository.getReactByAuthorAndPost(account, post);
        if(react!=null) react.setType(ReactType.DISLIKE);
        return react==null?
                reactRepository.save(React.builder().author(account).post(post).type(ReactType.DISLIKE).build()).getId():
                reactRepository.save(react).getId();
    }

    @Transactional
    public void deleteByAccountIdAndPostId(Long authorId, Long postId) {
        Account account = Account.builder().id(authorId).build();
        Post post = Post.builder().id(postId).build();
        reactRepository.deleteByAuthorAndPost(account, post);
    }

    public Long getPostLikes(Long id) {
        return reactRepository.countAllByPostAndType(Post.builder().id(id).build(), ReactType.LIKE);
    }

    public Long getPostDislikes(Long id) {
        return reactRepository.countAllByPostAndType(Post.builder().id(id).build(), ReactType.DISLIKE);
    }

    public ReactType getReactByUserAndPostId(Account user, Long id) {
        if(user==null) return null;
        React react = reactRepository.getReactByAuthorAndPost(
                Account.builder().id(user.getId()).build(),
                Post.builder().id(id).build()
        );
        System.out.println(react);
        if(react==null) return null;
        return react.getType();
    }

}
