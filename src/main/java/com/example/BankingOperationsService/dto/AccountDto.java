package com.example.BankingOperationsService.dto;

import com.example.BankingOperationsService.model.Account;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountDto {
    private Long id;
    private BigDecimal balance;

    public static AccountDto toAccountDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.id = account.getId();
        dto.balance = account.getBalance();
        return dto;
    }
}
