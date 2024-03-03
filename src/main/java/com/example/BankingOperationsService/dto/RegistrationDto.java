package com.example.BankingOperationsService.dto;

import com.example.BankingOperationsService.model.Account;
import com.example.BankingOperationsService.model.Contact;
import com.example.BankingOperationsService.model.ContactType;
import com.example.BankingOperationsService.model.User;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RegistrationDto {
    @NotBlank(message = "Полное имя не может быть пустым")
    private String fullName;

    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Телефон не может быть пустым")
    @Size(min = 10, message = "Номер телефона должен содержать минимум 10 цифр")
    private String phone;

    @NotNull(message = "Баланс не может быть пустым")
    private BigDecimal balance;

    public User toUser() {
        User user = new User();
        user.setFullName(this.fullName);
        user.setBirthDate(this.birthDate);
        user.setUsername(this.username);
        user.setPassword(this.password);

        user.setAccount(new Account(user, this.balance));

        List<Contact> contacts = new ArrayList<>();
        if (this.email != null) {
            contacts.add(new Contact(user, ContactType.EMAIL, this.email));
        }
        if (this.phone != null) {
            contacts.add(new Contact(user, ContactType.PHONE, this.phone));
        }
        user.setContacts(contacts);

        System.out.println(user + "qwerqwer");
        return user;
    }
}
