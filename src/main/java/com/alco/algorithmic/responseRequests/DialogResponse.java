package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.Dialog;
import com.alco.algorithmic.entity.Message;
import lombok.Data;

import java.util.List;

@Data
public class DialogResponse {

    private Long id;

    private String title;

    private Long icon;

    private MessageResponseLite lastMessage;

    public DialogResponse(Dialog dialog, Message message) {
        this.id = dialog.getId();
        this.title = dialog.getTitle();
        this.icon = dialog.getIcon() != null ? dialog.getIcon().getId() : null;
        this.lastMessage = message != null ? new MessageResponseLite(message) : null;
    }

}
