package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.Account;
import lombok.Data;

import java.util.Date;

@Data
public class AccountResponseForAdmin {

    private Long id;

    private String name;

    private String email;

    private String surname;

    private String status;

    private Long avatar;

    private Short indicator;

    private Date createdAt;

    public AccountResponseForAdmin(Account account) {
        id =  account.getId();
        name = account.getName();
        email = account.getEmail();
        surname = account.getSurname();
        status = account.getStatus();
        if(account.getAvatar()!=null)
            avatar = account.getAvatar().getId();
        indicator = account.getIndicator();
        createdAt = account.getCreatedAt();
    }
}
