package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.Message;
import lombok.Data;

import java.util.Date;

@Data
public class MessageResponseLite {

    private Long id;

    private String text;

    private AccountResponse author;

    private Date sentAt;

    public MessageResponseLite(Message message) {
        this.id = message.getId();
        this.text = message.getText();
        this.author = new AccountResponse(message.getAuthor());
        this.sentAt = message.getSentAt();
    }

}
