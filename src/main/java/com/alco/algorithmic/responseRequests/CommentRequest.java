package com.alco.algorithmic.responseRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank(message = "Field \"text\" cannot be empty")
    @Size(min = 1, max = 1024, message = "The length of field \"text\" must be between 1 and 1024")
    private String text;

}
