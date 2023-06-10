package com.alco.algorithmic.responseRequests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountNameRequest {

    @Size(max = 32, message = "The length of field \"firstname\" must be between 1 and 32")
    private String firstname;

    @Size(max = 32, message = "The length of field \"surname\" must be between 1 and 32")
    private String surname;

    @AssertTrue(message = "Field \"name\" and \"surname\" cannot be empty")
    public boolean check() {
        return (firstname!=null && !firstname.isEmpty()) || (surname!=null && !surname.isEmpty());
    }

}
