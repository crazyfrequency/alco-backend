package com.alco.algorithmic.controllers;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.exceptions.RequestBodyError;
import com.alco.algorithmic.service.DialogService;
import com.alco.algorithmic.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/icons")
public class IconController {
    private final FileService fileService;
    private final DialogService dialogService;

    @GetMapping("/null")
    public void getDefault(@NonNull HttpServletResponse response) throws FileNotFoundException, IOException {
        File file = new File(fileService.getPath()+"/default-icon.svg");
        response.setContentType("image/svg+xml");
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/{id}")
    public void getIcon(@PathVariable Long id, @NonNull HttpServletResponse response) throws IOException {
        fileService.getIconById(id, response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> setIcon(@PathVariable Long id, @RequestParam(value = "file") MultipartFile file) throws RequestBodyError {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long fileId = fileService.addIcon(file);
        boolean res = dialogService.setDialogIcon(id, account.getId(), fileId);
        if(!res)
            return new ResponseEntity<>("404", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PutMapping
    public Long addIcon(@RequestParam(value = "file") MultipartFile file) throws RequestBodyError {
        return fileService.addIcon(file);
    }

}
