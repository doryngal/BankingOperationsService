package com.example.BankingOperationsService.service;

import com.example.BankingOperationsService.dto.MessageDto;
import com.example.BankingOperationsService.dto.TransferDto;
import org.springframework.http.ResponseEntity;

public interface TransferService {
    ResponseEntity<MessageDto> transferMoney(TransferDto transferDto);

}
