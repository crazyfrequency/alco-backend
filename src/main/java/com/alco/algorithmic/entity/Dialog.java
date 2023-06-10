package com.alco.algorithmic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "dialogs")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne(targetEntity = File.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "icon", referencedColumnName = "id")
    private File icon;

    @ManyToMany(targetEntity = Account.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Account> users;

    @ManyToMany(targetEntity = Account.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Account> admins;

    @CreationTimestamp
    @Value("NOW()")
    private Date createdAt;

}
