package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.FileRepository;
import com.alco.algorithmic.entity.File;
import com.alco.algorithmic.enums.FileType;
import com.alco.algorithmic.exceptions.RequestBodyError;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    private final Path root = Paths.get("uploads");

    @PostConstruct
    public void init(){
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public String saveFile(MultipartFile file){
        String name = (new Date().toInstant()+".file").replaceAll(":", "-");
        try {
            Files.copy(file.getInputStream(), this.root.resolve(name));
        }catch (Exception e){
            if(e instanceof FileAlreadyExistsException)
                return saveFile(file);
        }
        return name;
    }

    public Long addAvatar(MultipartFile multipartFile) throws RequestBodyError {
        if(multipartFile.isEmpty()) throw new RequestBodyError("Empty file");
        if(!Objects.requireNonNull(multipartFile.getContentType()).startsWith("image/"))
            throw new RequestBodyError("File type not supported");
        String path = saveFile(multipartFile);
        return fileRepository.save(
                File.builder()
                        .path(path)
                        .type(FileType.AVATAR)
                        .name(multipartFile.getOriginalFilename())
                        .build()
        ).getId();
    }

    public Long addIcon(MultipartFile multipartFile) throws RequestBodyError {
        if(multipartFile.isEmpty()) throw new RequestBodyError("Empty file");
        if(!Objects.requireNonNull(multipartFile.getContentType()).startsWith("image/"))
            throw new RequestBodyError("File type not supported");
        String path = saveFile(multipartFile);
        return fileRepository.save(
                File.builder()
                        .path(path)
                        .type(FileType.ICON)
                        .name(multipartFile.getOriginalFilename())
                        .build()
        ).getId();
    }

    public List<Long> addFiles(List<MultipartFile> multipartFiles){
        List<File> files = multipartFiles.stream().map(multipartFile -> {
            String path = saveFile(multipartFile);
            return File.builder()
                    .name(multipartFile.getOriginalFilename())
                    .path(path)
                    .type(Objects.requireNonNull(multipartFile.getContentType()).startsWith("image/")?FileType.IMAGE:FileType.ANY)
                    .build();
        }).toList();
        return fileRepository.saveAll(files).stream().map(File::getId).toList();
    }

    public void getAvatarById(Long id, @NonNull HttpServletResponse response) throws IOException {
        File file = fileRepository.getFileById(id);
        if(file==null) throw new NotFoundException("File not found");
        if(file.getType()!=FileType.AVATAR) throw new NotFoundException("File not found");
        getResource(response, file);
    }

    public void getIconById(Long id, @NonNull HttpServletResponse response) throws IOException {
        File file = fileRepository.getFileById(id);
        if(file==null) throw new NotFoundException("File not found");
        if(file.getType()!=FileType.ICON) throw new NotFoundException("File not found");
        getResource(response, file);
    }

    public void getFileById(Long id, @NonNull HttpServletResponse response) throws IOException {
        File file = fileRepository.getFileById(id);
        if(file==null) throw new NotFoundException("File not found");
        if(file.getType()==FileType.AVATAR||file.getType()==FileType.ICON) throw new NotFoundException("File not found");
        getResource(response, file);
    }

    private void getResource(@NonNull HttpServletResponse response, File file) throws IOException {
        java.io.File fileObject = new java.io.File(root.resolve(file.getPath()).toAbsolutePath().toString());
        response.setContentType(Files.probeContentType(fileObject.toPath()));
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) fileObject.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(fileObject));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    public String getPath(){
        return root.toAbsolutePath().toString();
    }

}
