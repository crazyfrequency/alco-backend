package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Friends;
import com.alco.algorithmic.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface FriendsRepository extends JpaRepository<Friends,Long> {

    List<Friends> getFriendsByFromOrTo(Long from, Long to);

    @Query(value = """
        select u.id from Account u where
            u.id in (select f.from from Friends f where f.to = (select u from Account u where u.id = :id) and f.status = com.alco.algorithmic.enums.FriendStatus.FRIEND) or
            u.id in (select f.to from Friends f where f.from = (select u from Account u where u.id = :id) and f.status = com.alco.algorithmic.enums.FriendStatus.FRIEND)
    """)
    List<Long> getFriendsByAccountId(Long id);

    @Query(value = """
        select u.id from Account u where
            (
                u.id in (select f.from from Friends f where f.to = (select u from Account u where u.id = :id) and f.status = com.alco.algorithmic.enums.FriendStatus.FRIEND) or
                u.id in (select f.to from Friends f where f.from = (select u from Account u where u.id = :id) and f.status = com.alco.algorithmic.enums.FriendStatus.FRIEND)
            ) and u.createdAt>=:last
    """)
    List<Long> getEvent(Long id, Date last);

    boolean existsByIdAndFromAndStatus(Long id, Account from, FriendStatus status);

    Friends findFirstByFromAndToAndStatus(Account from, Account to, FriendStatus status);

    Friends findFirstByFromInAndToInAndStatus(Account[] from, Account[] to, FriendStatus status);

}
