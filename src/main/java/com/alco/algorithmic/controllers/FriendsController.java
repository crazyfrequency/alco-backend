package com.alco.algorithmic.controllers;

import com.alco.algorithmic.dao.FriendsRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Friends;
import com.alco.algorithmic.enums.FriendStatus;
import com.alco.algorithmic.responseRequests.AccountResponseFull;
import com.alco.algorithmic.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/friends")
public class FriendsController {
    @Autowired
    private final AccountService accountService;
    @Autowired
    public FriendsRepository friendsRepository;

    @GetMapping()
    public List<AccountResponseFull> getFriends() {
        var account = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountService.getFriendsByAccountId(account.getId());
    }

    @GetMapping("/{id}")
    public List<AccountResponseFull> getFriendsByUserId(@PathVariable Long id) {
        return accountService.getFriendsByAccountId(id);
    }

    @GetMapping("/requests/my")
    public List<AccountResponseFull> getMyFriendsRequests() {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountService.getMyFriendsRequestsByAccountId(account.getId());
    }

    @PostMapping("/requests/{id}")
    public ResponseEntity<String> confirmFriend(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Friends friends = friendsRepository.findFirstByFromAndToAndStatus(Account.builder().id(id).build(), account, FriendStatus.INVITE);
        friends.setStatus(FriendStatus.FRIEND);
        friends.setSentAt(new Date());
        friendsRepository.save(friends);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<String> removeFriendRequest(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var fr = friendsRepository.findFirstByFromAndToAndStatus(
                Account.builder().id(account.getId()).build(),
                Account.builder().id(id).build(),
                FriendStatus.INVITE
        );
        if(fr!=null){
            friendsRepository.deleteById(fr.getId());
            return ResponseEntity.ok("OK");
        }
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/requests")
    public List<AccountResponseFull> getFriendsRequests() {
        var account = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountService.getFriendsRequestsByAccountId(account.getId());
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> addFriend(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(Objects.equals(account.getId(), id))
            return new ResponseEntity<>("You can't add yourself as a friend", HttpStatus.CONFLICT);
        String status = accountService.addFriend(account.getId(), id);
        if (status == null)
            return ResponseEntity.status(404).body("Can't send friend request");
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFriend(@PathVariable Long id) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account[] accounts = {account, Account.builder().id(id).build()};
        Friends status = friendsRepository.findFirstByFromInAndToInAndStatus(accounts, accounts, FriendStatus.FRIEND);
        if (status == null)
            return ResponseEntity.status(404).body("Can't delete friend");
        friendsRepository.deleteById(status.getId());
        return ResponseEntity.ok("ok");
    }

}
