package com.alco.algorithmic.controllers;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.exceptions.RequestBodyError;
import com.alco.algorithmic.responseRequests.MessageRequest;
import com.alco.algorithmic.responseRequests.MessageResponse;
import com.alco.algorithmic.service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/chats")
@Validated
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/{id}/messages")
    public List<MessageResponse> getMessages(@PathVariable Long id, @RequestParam(name="offset", required = false) Long offset) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return messageService.getMessages(id, account.getId(), offset);
    }

    @PutMapping("/{id}/messages")
    public Long addMessage(@PathVariable Long id, @Valid @RequestBody MessageRequest message) throws RequestBodyError {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return messageService.addMessage(id, account.getId(), message);
    }

    @PatchMapping("/{dialogId}/messages/{messageId}")
    public ResponseEntity<String> editMessage(@PathVariable Long dialogId, @PathVariable Long messageId, @Valid @RequestBody MessageRequest message) throws RequestBodyError {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean res = messageService.editMessage(dialogId, account.getId(), messageId, message);
        if(res) return new ResponseEntity<>("OK", HttpStatus.OK);
        return new ResponseEntity<>("Nothing new", HttpStatus.CONFLICT);
    }

}
