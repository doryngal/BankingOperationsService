package com.example.BankingOperationsService.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchDto {
    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotBlank(message = "Телефон не может быть пустым")
    @Size(min = 10, max = 15, message = "Номер телефона должен быть от 10 до 15 цифр")
    private String phone;

    @NotBlank(message = "Полное имя не может быть пустым")
    private String fullName;

    @Email(message = "Некорректный формат email")
    private String email;
}
