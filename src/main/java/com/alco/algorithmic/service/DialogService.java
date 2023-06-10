package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.DialogRepository;
import com.alco.algorithmic.dao.MessageRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Dialog;
import com.alco.algorithmic.entity.File;
import com.alco.algorithmic.entity.Message;
import com.alco.algorithmic.responseRequests.DialogRequest;
import com.alco.algorithmic.responseRequests.DialogResponse;
import com.alco.algorithmic.responseRequests.DialogResponseFull;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DialogService {
    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;

    public Long addDialog(DialogRequest dialogRequest, Long author) {
        Account user = Account.builder().id(author).build();
        List<Account> users = dialogRequest.getUsers().stream().map(
                aLong -> Account.builder().id(aLong).build()
        ).collect(Collectors.toList());
        users.add(user);
        var dialog = Dialog.builder()
                .admins(List.of(new Account[]{user}))
                .title(dialogRequest.getTitle())
                .users(users);
        if(dialogRequest.getIcon()!=null) dialog.icon(File.builder().id(dialogRequest.getIcon()).build());
        return dialogRepository.save(dialog.build()).getId();
    }

    @Transactional
    public boolean setDialogIcon(Long id, Long account, Long file) {
        if(!dialogRepository.existsByIdAndAdminsContaining(id, Account.builder().id(account).build()))
            return false;
        return Objects.equals(
                dialogRepository.save(
                        Dialog.builder()
                                .id(id)
                                .icon(
                                        File.builder()
                                                .id(file)
                                                .build()
                                ).build()
                ).getIcon().getId(),
                file
        );
    }

    public boolean deleteDialog(Long id, Long account) {
        return dialogRepository.deleteByIdAndAdminsContaining(
                id,
                Account.builder().id(account).build()
        )!=0;
    }

    public DialogResponseFull getDialog(Long id, Long account) {
        return new DialogResponseFull(dialogRepository.getDialogByIdAndUsersContaining(
                id,
                Account.builder().id(account).build()
        ));
    }

    public List<DialogResponse> getUserDialogs(Long id, Long offset) {
        List<Dialog> dialogs = dialogRepository.findDialogsByUsersContains(
                Account.builder().id(id).build()
        );
        return dialogs.stream().map(dialog -> {
            Message message = messageRepository.getLastByDialogId(dialog.getId());
            return new DialogResponse(dialog, message);
        }).toList();
    }

}
