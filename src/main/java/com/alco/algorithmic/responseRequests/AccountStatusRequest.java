package com.alco.algorithmic.responseRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountStatusRequest {

    @NotBlank(message = "Field \"status\" cannot be empty")
    @Size(max = 128, message = "The length of field \"status\" must be between 1 and 128")
    private String status;

}
