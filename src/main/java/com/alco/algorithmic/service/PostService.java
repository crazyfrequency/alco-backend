package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.FriendsRepository;
import com.alco.algorithmic.dao.PostRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.File;
import com.alco.algorithmic.entity.Post;
import com.alco.algorithmic.responseRequests.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FriendsRepository friendsRepository;

    public Post addPostByAccountId(Long id, PostRequest post) {
        List<File> files = post.getFiles() != null ? post.getFiles().stream().map(aLong -> File.builder().id(aLong).build()).toList() : null;
        Post newPost = Post.builder()
                .title(post.getTitle())
                .text(post.getText())
                .files(files)
                .author(Account.builder().id(id).build())
                .build();
        return postRepository.save(newPost);
    }

    public Post getPostById(Long id) {
        return postRepository.getPostById(id);
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> getPostsByAccountId(Long id, Long offset) {
        if (offset == null)
            return postRepository.findFirst100ByAccountIdOrderById(id);
        return postRepository.findFirst100ByAccountIdAfterOrderById(id, offset);
    }

    public List<Post> getFriendsPostsByAccountId(Long id, Long offset) {
        List<Long> ids = friendsRepository.getFriendsByAccountId(id);
        if (offset == null)
            return postRepository.findFirst100ByAccountsIdOrderById(ids);
        return postRepository.findFirst100ByAccountsIdAfterOrderById(ids, offset);
    }

}
