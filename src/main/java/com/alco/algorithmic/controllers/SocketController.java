package com.alco.algorithmic.controllers;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Post;
import com.alco.algorithmic.responseRequests.PostRequest;
import com.alco.algorithmic.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;


@Controller
public class SocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private PostService postService;

    @RequestMapping(path = "/post", method = RequestMethod.POST)
    public void processPosts(@RequestBody String data){
        messagingTemplate.convertAndSend("/topic/greetings", data);
    }

    @MessageMapping("/chat")
    public void greeting(String s) throws Exception {
        System.out.println("ok");
        System.out.println(s);
        messagingTemplate.convertAndSend("/chat", "123");
    }

}
