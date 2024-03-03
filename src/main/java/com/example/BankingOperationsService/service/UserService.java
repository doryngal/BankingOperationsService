package com.example.BankingOperationsService.service;

import com.example.BankingOperationsService.dto.ContactDto;
import com.example.BankingOperationsService.dto.MessageDto;
import com.example.BankingOperationsService.dto.SearchDto;
import com.example.BankingOperationsService.dto.UserDto;
import com.example.BankingOperationsService.model.User;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserDto getUserById(Long userId);
    ResponseEntity<MessageDto> addContact(ContactDto contactDto);
    ResponseEntity<MessageDto> updateContact(Long contactId, ContactDto contactDto);
    ResponseEntity<?> deleteUserContact(Long contractId);

    List<UserDto> findByBirthDateAfter(LocalDate date);
    List<UserDto> findByFullName(String fullName);
    UserDto findByEmail(String email);
    UserDto findByPhone(String phone);

}
