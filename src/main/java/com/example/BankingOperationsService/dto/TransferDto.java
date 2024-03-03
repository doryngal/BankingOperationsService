package com.example.BankingOperationsService.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDto {
    @NotNull(message = "Идентификатор счета отправителя не может быть пустым")
    private Long fromAccountId;

    @NotNull(message = "Идентификатор счета получателя не может быть пустым")
    private Long toAccountId;

    @NotNull(message = "Сумма для перевода не может быть пустой")
    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше нуля")
    private BigDecimal amount;
}

