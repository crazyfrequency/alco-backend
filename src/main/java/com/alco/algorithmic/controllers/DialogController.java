package com.alco.algorithmic.controllers;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.responseRequests.DialogRequest;
import com.alco.algorithmic.responseRequests.DialogResponse;
import com.alco.algorithmic.responseRequests.DialogResponseFull;
import com.alco.algorithmic.service.DialogService;
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
public class DialogController {

    private final DialogService dialogService;

    @PutMapping
    public Long addChat(@Valid @RequestBody DialogRequest dialog) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return dialogService.addDialog(dialog, account.getId());
    }

    @GetMapping
    public List<DialogResponse> getDialogs(@RequestParam(name="offset", required = false) Long offset){
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return dialogService.getUserDialogs(account.getId(), offset);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean status = dialogService.deleteDialog(id, account.getId());
        if(status) return ResponseEntity.ok("OK");
        return new ResponseEntity<>("Can not be removed", HttpStatus.CONFLICT);
    }

    @GetMapping("/{id}")
    public DialogResponseFull getDialog(@PathVariable Long id){
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return dialogService.getDialog(id, account.getId());
    }

}
