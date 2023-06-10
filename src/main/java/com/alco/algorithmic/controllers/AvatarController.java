package com.alco.algorithmic.controllers;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.exceptions.RequestBodyError;
import com.alco.algorithmic.service.AccountService;
import com.alco.algorithmic.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/avatars")
public class AvatarController {
    private final FileService fileService;
    private final AccountService accountService;

    @GetMapping("/null")
    public void getDefault(@NonNull HttpServletResponse response) throws FileNotFoundException, IOException {
        File file = new File(fileService.getPath()+"/default.svg");
        response.setContentType("image/svg+xml");
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/{id}")
    public void getAvatar(@PathVariable Long id, @NonNull HttpServletResponse response) throws IOException {
        fileService.getAvatarById(id, response);
    }

    @PutMapping
    public Long loadAvatar(@RequestParam(value = "file") MultipartFile file) throws RequestBodyError {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = fileService.addAvatar(file);
        return id;
    }

    @PostMapping
    public Long setAvatar(@RequestParam(value = "file") MultipartFile file) throws RequestBodyError {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = fileService.addAvatar(file);
        accountService.setAvatar(account.getId(), id);
        return id;
    }

}

