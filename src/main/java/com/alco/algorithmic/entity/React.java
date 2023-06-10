package com.alco.algorithmic.entity;

import com.alco.algorithmic.enums.ReactType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table(name = "reacts")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class React {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Account.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Account author;

    @OneToOne(targetEntity = Post.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private ReactType type = ReactType.NONE;

}
