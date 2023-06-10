package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.Dialog;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DialogResponseFull {

    private Long id;

    private String title;

    private Long icon;

    private List<AccountResponse> users;

    private List<AccountResponse> admins;

    private Date createdAt;

    public DialogResponseFull(Dialog dialog) {
        this.id = dialog.getId();
        this.title = dialog.getTitle();
        this.icon = dialog.getIcon() != null ? dialog.getIcon().getId() : null;
        this.users = dialog.getUsers().stream().map(AccountResponse::new).toList();
        this.admins = dialog.getAdmins().stream().map(AccountResponse::new).toList();
        this.createdAt = dialog.getCreatedAt();
    }

}
