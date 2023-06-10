package com.alco.algorithmic.responseRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class DialogRequest {

    @NotBlank(message = "Field \"title\" cannot be empty")
    private String title;

    private Long icon;

    @NotEmpty(message = "Field \"users\" cannot be empty")
    private List<Long> users;

}
