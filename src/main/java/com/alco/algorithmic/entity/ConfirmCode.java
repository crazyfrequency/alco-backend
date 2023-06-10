package com.alco.algorithmic.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Getter
@Setter
@Table(name = "confirm_keys")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmCode {
    @Id
    @Column(nullable = false)
    private String key;

    @OneToOne(targetEntity = Account.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Account user;

    @CreationTimestamp
    @Value("NOW()")
    private Date createdAt;

}
