package com.alco.algorithmic.controllers;

import com.alco.algorithmic.dao.FriendsRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.responseRequests.MessageResponse;
import com.alco.algorithmic.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/events")
public class LongPollController {
    private final static Long LONG_POLLING_TIMEOUT = 5000L;
    private ExecutorService bakers;
    @Autowired
    public FriendsRepository friendsRepository;
    @Autowired
    public MessageService messageService;

    public LongPollController() {
        bakers = Executors.newFixedThreadPool(5);
    }

    @GetMapping("/friends/{lastCreatedAt}")
    public DeferredResult<List<Long>> publisher(@PathVariable Long lastCreatedAt, @RequestParam Integer t) {

        DeferredResult<List<Long>> output = new DeferredResult<>(LONG_POLLING_TIMEOUT);
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        bakers.execute(() -> {
            try {
                Thread.sleep(t);
                var res = friendsRepository.getEvent(account.getId(), new Date(lastCreatedAt));
                output.setResult(res);
            } catch (Exception e) {
                output.setErrorResult("Something went wrong with your order!");
            }
        });

        output.onTimeout(() -> output.setErrorResult("the bakery is not responding in allowed time"));
        return output;
    }

    @GetMapping("/messages/{id}")
    public DeferredResult<List<MessageResponse>> publisher(@PathVariable Long id, @RequestParam Long lastId, @RequestParam Integer t) {

        DeferredResult<List<MessageResponse>> output = new DeferredResult<>(LONG_POLLING_TIMEOUT);
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        bakers.execute(() -> {
            try {
                Thread.sleep(t);
                var res = messageService.getEvent(id, account.getId(), lastId);
                output.setResult(res);
            } catch (Exception e) {
                output.setErrorResult("Something went wrong with your order!");
            }
        });

        output.onTimeout(() -> output.setErrorResult("the bakery is not responding in allowed time"));
        return output;
    }

}
