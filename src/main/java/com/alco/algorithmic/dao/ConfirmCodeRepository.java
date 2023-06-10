package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.ConfirmCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, String> {

    public boolean existsByKey(String key);

    public ConfirmCode findFirstByUserOrderByCreatedAt(Account user);

    public ConfirmCode findByKey(String key);

}
