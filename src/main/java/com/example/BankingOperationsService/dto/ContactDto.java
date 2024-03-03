package com.example.BankingOperationsService.dto;

import com.example.BankingOperationsService.model.Contact;
import com.example.BankingOperationsService.model.ContactType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ContactDto {
    @NotBlank(message = "Тип контакта обязателен")
    private ContactType type; // EMAIL или PHONE
    @NotBlank(message = "Требуется значение контакта")
    private String value;

    public static List<ContactDto> toContactDtoList(List<Contact> contacts) {
        return contacts.stream().map(contact -> {
            ContactDto dto = new ContactDto();
            dto.setType(contact.getType());
            dto.setValue(contact.getValue());
            return dto;
        }).collect(Collectors.toList());
    }
}
