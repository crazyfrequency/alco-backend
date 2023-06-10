package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsById(Long id);

    Account getAccountById(Long id);

    Optional<Account> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update users set avatar = :avatar where id = :id", nativeQuery = true)
    void setAvatarByUserId(Long id, Long avatar);

    @Transactional
    @Modifying
    @Query("update Account u set u.name = :name where u.id = :id")
    int setNameByUserId(Long id, String name);

    @Transactional
    @Modifying
    @Query("update Account u set u.surname = :surname where u.id = :id")
    int setSurnameByUserId(Long id, String surname);

    @Transactional
    @Modifying
    @Query("update Account u set u.status = :status where u.id = :id")
    int setStatusByUserId(Long id, String status);

    @Query(value = "update users set role = 'USER' where id = :id and role = 'NEW' returning 1", nativeQuery = true)
    int setConfirmById(Long id);

    @Query("""
        select u from Account u where
            u.id in (select f.from from Friends f where f.to = (select u from Account u where u.id = :id) and f.status = com.alco.algorithmic.enums.FriendStatus.FRIEND) or
            u.id in (select f.to from Friends f where f.from = (select u from Account u where u.id = :id) and f.status = com.alco.algorithmic.enums.FriendStatus.FRIEND)
    """)
    List<Account> getFriendsByUserId(Long id);

    @Query("select u from Account u where u.id in (select f.from from Friends f where f.to = (select u from Account u where u.id = :id) and f.status = com.alco.algorithmic.enums.FriendStatus.INVITE)")
    List<Account> getFriendsRequestsByUserId(Long id);

    @Query("select u from Account u where concat(u.name, ' ', u.surname) ilike concat('%', :text, '%') order by u.id limit 100")
    List<Account> findByNameAndSurname(String text);

    @Query("select u from Account u where concat(u.name, ' ', u.surname) ilike concat('%', :text, '%') and u.id>:offset order by u.id limit 100")
    List<Account> findByNameAndSurnameWithOffset(String text, Long offset);

    @Query("select u from Account u where u.id in (select f.to from Friends f where f.from = (select u from Account u where u.id = :id) and f.status = com.alco.algorithmic.enums.FriendStatus.INVITE)")
    List<Account> getMyFriendsRequestsByUserId(Long id);

    @Query(value = """
        insert into friends (from_id, to_id, sent_at, status)
        (select :from, :to, NOW(), :status
            where not exists (select 1 from friends where to_id = :from and from_id = :to) and
            not exists (select 1 from friends where to_id = :to and from_id = :from)
        ) returning status
    """, nativeQuery = true)
    String addFriend(Long from, Long to, String status);

}
