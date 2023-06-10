package com.alco.algorithmic.responseRequests;

import com.alco.algorithmic.entity.File;
import com.alco.algorithmic.enums.FileType;
import lombok.Data;

@Data
public class FileResponse {

    private Long id;

    private String name;

    private FileType type;

    public FileResponse(File file) {
        this.id = file.getId();
        this.name = file.getName();
        this.type = file.getType();
    }

}
