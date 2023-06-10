package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.Account;
import lombok.Data;

import java.util.Date;

@Data
public class AccountResponseFull {

    private Long id;

    private String name;

    private String surname;

    private String status;

    private Long avatar;

    private String role;

    private Short indicator;

    private Date createdAt;

    public AccountResponseFull(Account account) {
        id =  account.getId();
        name = account.getName();
        surname = account.getSurname();
        status = account.getStatus();
        role = account.getRole().name();
        if(account.getAvatar()!=null)
            avatar = account.getAvatar().getId();
        indicator = account.getIndicator();
        createdAt = account.getCreatedAt();
    }
}
