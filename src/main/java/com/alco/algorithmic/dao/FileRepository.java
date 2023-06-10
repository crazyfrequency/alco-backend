package com.alco.algorithmic.dao;

import com.alco.algorithmic.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {

    File getFileById(Long id);

    List<File> getFilesByIdIn(List<Long> ids);

}
