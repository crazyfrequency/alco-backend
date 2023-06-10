package com.alco.algorithmic.entity;

import com.alco.algorithmic.enums.FriendStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Getter
@Setter
@Table(name = "friends")
@Entity
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Account.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "from_id", referencedColumnName = "id")
    private Account from;

    @OneToOne(targetEntity = Account.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "to_id", referencedColumnName = "id")
    private Account to;

    @Enumerated(EnumType.STRING)
    private FriendStatus status = FriendStatus.INVITE;

    @CreationTimestamp
    @Column(nullable = false)
    @Value("NOW()")
    private Date sentAt;

}
