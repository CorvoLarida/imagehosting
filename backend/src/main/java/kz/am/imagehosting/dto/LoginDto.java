package kz.am.imagehosting.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO (@NotEmpty String username, @NotEmpty String password){}
