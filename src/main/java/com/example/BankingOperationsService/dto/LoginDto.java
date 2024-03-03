package com.example.BankingOperationsService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;
}
