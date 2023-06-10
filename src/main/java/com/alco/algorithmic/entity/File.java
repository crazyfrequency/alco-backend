package com.alco.algorithmic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;
import com.alco.algorithmic.enums.FileType;

import java.util.Date;

@Getter
@Setter
@Table(name = "files")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256, nullable = false)
    private String name;

    @Column(length = 1024, nullable = false)
    private String path;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type = FileType.ANY;

    @CreationTimestamp
    @Value("NOW()")
    private Date createdAt;
}
