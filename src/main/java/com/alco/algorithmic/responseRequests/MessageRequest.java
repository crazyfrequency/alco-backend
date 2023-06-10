package com.alco.algorithmic.responseRequests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MessageRequest {

    @Size(max = 4096, message = "The length of field \"text\" must be between 1 and 4096")
    private String text;

    private List<Long> files;

    @AssertTrue(message = "Field \"text\" or \"files\" cannot be empty")
    private boolean isTextOrFilesExists() {
        return !(text==null || text.isEmpty()) || !(files==null || files.isEmpty());
    }

}
