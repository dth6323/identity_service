package com.hadz.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
@Data
public class UserCreationRequest {
    @Size(min = 3,message = "UserName must be at least 3 character")
    private String username;
    @Size(min=8,message = "Password must be at least 8 character")
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
}
