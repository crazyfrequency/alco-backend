package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.DialogRepository;
import com.alco.algorithmic.dao.MessageRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Dialog;
import com.alco.algorithmic.entity.File;
import com.alco.algorithmic.entity.Message;
import com.alco.algorithmic.responseRequests.MessageRequest;
import com.alco.algorithmic.responseRequests.MessageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final DialogRepository dialogRepository;

    @Transactional
    public List<MessageResponse> getMessages(Long dialogId, Long userId, Long offset) {
        if(!dialogRepository.existsByIdAndUsersContaining(dialogId, Account.builder().id(userId).build()))
            return null;
        if(offset==null)
            return messageRepository.findFirst100ByDialogIdAndUserId(dialogId)
                    .stream().map(MessageResponse::new).toList();
        return messageRepository.findFirst100ByDialogIdAndUserIdWithOffset(dialogId, offset)
                .stream().map(MessageResponse::new).toList();
    }

    @Transactional
    public Long addMessage(Long dialogId, Long authorId, MessageRequest message) {
        if(!dialogRepository.existsByIdAndUsersContaining(
                dialogId,
                Account.builder()
                        .id(authorId)
                        .build()
        )) return null;
        return messageRepository.save(Message.builder()
                        .author(Account.builder()
                                .id(authorId)
                                .build())
                        .dialog(Dialog.builder()
                                .id(dialogId)
                                .build())
                        .text(message.getText())
                        .files(
                                message.getFiles()!=null ?
                                message.getFiles().stream()
                                        .map(fileId ->
                                                File.builder().id(fileId).build()
                                        ).toList() : null
                        )
                .build()).getId();
    }

    public boolean deleteMessage(Long dialogId, Long authorId, Long messageId) {
        return messageRepository.deleteByIdAndDialogAndAuthor(
                messageId,
                Dialog.builder().id(dialogId).build(),
                Account.builder().id(authorId).build()
        )!=0;
    }

    @Transactional
    public List<MessageResponse> getEvent(Long chatId, Long userId, Long lastId) {
        if(!dialogRepository.existsByIdAndUsersContaining(chatId, Account.builder().id(userId).build()))
            return null;
        return messageRepository.getEvent(chatId, lastId).stream().map(MessageResponse::new).toList();
    }

    @Transactional
    public boolean editMessage(Long dialogId, Long authorId, Long messageId, MessageRequest message) {
        Message message1 = messageRepository.getMessageByIdAndAuthorAndDialog(
                messageId,
                Account.builder().id(authorId).build(),
                Dialog.builder().id(dialogId).build()
        );
        if(message1==null) return false;
        if(
                Objects.equals(message.getText(), message1.getText())
                        &&
                message1.getFiles().stream().map(File::getId).toList().equals(message.getFiles())
        ) return true;
        return messageRepository.save(Message.builder()
                        .id(message1.getId())
                        .text(message.getText()!=null ? message.getText() : message1.getText())
                        .files(message.getFiles()!=null ?
                                message.getFiles().stream()
                                        .map(
                                                file -> File.builder()
                                                        .id(file)
                                                        .build()
                                                ).toList()
                                : message1.getFiles())
                        .edited(true)
                .build())!=null;
    }

}
