package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
        select t from Token t inner join Account u
        on t.user.id = u.id
        where u.id = :id and t.revoked = false
    """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}
