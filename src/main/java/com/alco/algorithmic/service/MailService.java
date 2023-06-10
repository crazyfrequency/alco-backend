package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.ConfirmCodeRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.ConfirmCode;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("MailService")
public class MailService {
    private Environment env;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ConfirmCodeRepository confirmCodeRepository;

    @Autowired
    public MailService(Environment env) {
        this.env = env;
    }

    @Async
    public void sendRegMail(String address, Account account) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");
            message.setFrom(env.getProperty("spring.mail.username"));
            message.setTo(address);
            message.setSubject("Активация аккаунта");
            String generatedString = null;
            while (true){
                generatedString = RandomStringUtils.random(64, true, true);
                if(!confirmCodeRepository.existsByKey(generatedString))
                    break;
            }
            ConfirmCode confirmCode = new ConfirmCode().builder()
                    .key(generatedString)
                    .user(account)
                    .build();
            confirmCodeRepository.save(confirmCode);
            message.setText("http://"+env.getProperty("alco.data.domain")+"/confirm/"+generatedString);

            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
