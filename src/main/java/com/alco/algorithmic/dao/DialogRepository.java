package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog,Long> {

    boolean existsByIdAndUsersContaining(Long id, Account users);

    boolean existsByIdAndAdminsContaining(Long id, Account admins);

    int deleteByIdAndAdminsContaining(Long id, Account account);

    Dialog getDialogByIdAndUsersContaining(Long id, Account account);

    List<Dialog> findDialogsByUsersContains(Account users);

//    @Query("select d from Dialog d where :id in d.users order by d.id desc limit 100")
//    List<Dialog> findFirst100ByUserId(List<Long> id);
//
//    @Query("select d from Dialog d where :id in d.users and d.id<:offset order by d.id desc limit 100")
//    List<Dialog> findFirst100ByUserIdWithOffset(List<Long> id, Long offset);

}
