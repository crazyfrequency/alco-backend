package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.Account;
import lombok.Data;

@Data
public class AccountResponse {

    private Long id;

    private String name;

    private String surname;

    private Long avatar;

    private Short indicator;

    public AccountResponse(Account account) {
        id =  account.getId();
        name = account.getName();
        surname = account.getSurname();
        if(account.getAvatar()!=null)
            avatar = account.getAvatar().getId();
        indicator = account.getIndicator();
    }
}
