package com.alco.algorithmic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "posts")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Account.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Account author;

    @Column(length = 256)
    private String title;

    @Column(length = 4096)
    private String text;

    @ManyToMany(targetEntity = File.class, cascade = CascadeType.MERGE)
    private List<File> files;

    @CreationTimestamp
    @Value("NOW()")
    private Date createdAt;
}
