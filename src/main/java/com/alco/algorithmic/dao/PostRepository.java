package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.author.id = :id and p.id<:offset order by p.id desc limit 100")
    List<Post> findFirst100ByAccountIdAfterOrderById(Long id, long offset);

    @Query("select p from Post p where p.author.id = :id order by p.id desc limit 100")
    List<Post> findFirst100ByAccountIdOrderById(Long id);

    @Query("select p from Post p where p.author.id in :ids and p.id<:offset order by p.id desc limit 100")
    List<Post> findFirst100ByAccountsIdAfterOrderById(List<Long> ids, long offset);

    @Query("select p from Post p where p.author.id in :ids order by p.id desc limit 100")
    List<Post> findFirst100ByAccountsIdOrderById(List<Long> ids);

    Post getPostById(Long id);

}
