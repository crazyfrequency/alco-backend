package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.File;
import com.alco.algorithmic.entity.Message;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MessageResponse {

    private Long id;

    private AccountResponse author;

    private Long dialog;

    private String text;

    private List<Long> files;

    private Date sentAt;

    private boolean edited;

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.author = new AccountResponse(message.getAuthor());
        this.dialog = message.getDialog().getId();
        this.text = message.getText();
        this.files = message.getFiles() != null ? message.getFiles().stream().map(File::getId).toList() : null;
        this.sentAt = message.getSentAt();
        this.edited = message.getEdited()!= null ? message.getEdited() : false;
    }

}
