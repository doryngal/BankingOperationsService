package com.example.BankingOperationsService.dto;

import com.example.BankingOperationsService.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private List<ContactDto> contacts;
    private AccountDto account;


    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setBirthDate(user.getBirthDate());

        if (user.getContacts() != null) {
            dto.setContacts(ContactDto.toContactDtoList(user.getContacts()));
        }

        if (user.getAccount() != null) {
            dto.setAccount(AccountDto.toAccountDto(user.getAccount()));
        }

        return dto;
    }
}

