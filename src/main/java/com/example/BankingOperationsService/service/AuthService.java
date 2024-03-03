package com.example.BankingOperationsService.service;

import com.example.BankingOperationsService.dto.LoginDto;
import com.example.BankingOperationsService.dto.MessageDto;
import com.example.BankingOperationsService.dto.RegistrationDto;
import com.example.BankingOperationsService.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {
    ResponseEntity<MessageDto> register(User user);
    ResponseEntity<?> authenticate(LoginDto loginDto);
}
