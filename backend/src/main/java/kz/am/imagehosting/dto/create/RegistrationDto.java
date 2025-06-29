package kz.am.imagehosting.dto.create;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public class RegistrationDto {
    private UUID id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
