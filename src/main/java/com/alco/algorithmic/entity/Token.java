package com.alco.algorithmic.entity;

import com.alco.algorithmic.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table(name = "tokens")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Account user;

}
