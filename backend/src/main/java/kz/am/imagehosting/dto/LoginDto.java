package kz.am.imagehosting.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
  
}
