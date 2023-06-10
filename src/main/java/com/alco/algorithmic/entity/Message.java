package com.alco.algorithmic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "messages")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(targetEntity = Account.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Account author;

    @OneToOne(targetEntity = Dialog.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id", referencedColumnName = "id")
    private Dialog dialog;

    @Column(length = 4096)
    private String text;

    @ManyToMany(targetEntity = File.class, cascade = CascadeType.MERGE)
    private List<File> files;

    @CreationTimestamp
    @Value("NOW()")
    private Date sentAt;

    @Value("#{false}")
    private Boolean edited;

}
