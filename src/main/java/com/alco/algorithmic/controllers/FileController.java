package com.alco.algorithmic.controllers;

import com.alco.algorithmic.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private final FileService fileService;

    @PutMapping()
    public List<Long> addFiles(@RequestParam(value = "files") List<MultipartFile> files){
        return fileService.addFiles(files);
    }

    @GetMapping("/{id}")
    public void getFile(@PathVariable Long id, @NonNull HttpServletResponse response) throws IOException {
        fileService.getFileById(id, response);
    }

}
