package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Dialog;
import com.alco.algorithmic.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.dialog.id = :id order by m.id desc limit 1")
    Message getLastByDialogId(Long id);

    @Query("select m from Message m where m.dialog.id = :dialog order by m.id desc limit 100")
    List<Message> findFirst100ByDialogIdAndUserId(Long dialog);

    @Query("select m from Message m where m.dialog.id = :dialog and m.id<:offset order by m.id desc limit 100")
    List<Message> findFirst100ByDialogIdAndUserIdWithOffset(Long dialog, Long offset);

    @Query("select m from Message m where m.dialog.id = :chatId and m.id > :lastId")
    List<Message> getEvent(Long chatId, Long lastId);

    @Transactional
    int deleteByIdAndDialogAndAuthor(Long id, Dialog dialog, Account author);

    Message getMessageByIdAndAuthorAndDialog(Long id, Account author, Dialog dialog);

}