package com.example.BankingOperationsService.service;

import com.example.BankingOperationsService.dto.MessageDto;
import com.example.BankingOperationsService.dto.TransferDto;
import com.example.BankingOperationsService.model.Account;
import com.example.BankingOperationsService.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class TransferServiceImpl implements TransferService {

    private AccountRepository accountRepository;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(value = "accountBalances", allEntries = true)
    public ResponseEntity<MessageDto> transferMoney(TransferDto transferDto) {
        try {
            log.info("Начало операции перевода средств с счета {} на счет {}", transferDto.getFromAccountId(), transferDto.getToAccountId());
            Account fromAccount = accountRepository.findById(transferDto.getFromAccountId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Счет отправителя не найден"));

            Account toAccount = accountRepository.findById(transferDto.getToAccountId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Счет получателя не найден"));

            if (fromAccount.getBalance().compareTo(transferDto.getAmount()) < 0) {
                log.error("Недостаточно средств на счете отправителя с ID {}", transferDto.getFromAccountId());
                return ResponseEntity.badRequest().body(new MessageDto("Недостаточно средств на счете отправителя"));
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(transferDto.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(transferDto.getAmount()));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            log.info("Перевод средств выполнен успешно");
            return ResponseEntity.ok(new MessageDto("Перевод средств выполнен успешно"));
        } catch (Exception e) {
            log.error("Ошибка при переводе средств", e);
            return ResponseEntity.internalServerError().body(new MessageDto("Ошибка при обработке вашего запроса"));
        }
    }

    @Scheduled(fixedRate = 60000) // 60000 миллисекунд = 1 минута
    @CacheEvict(value = "accountBalances", allEntries = true)
    public void updateBalance() {
        try {
            log.info("Начало операции обновления баланса счетов");
            List<Account> accounts = accountRepository.findAll();
            accounts.forEach(account -> {
                BigDecimal increasedAmount = account.getBalance().multiply(new BigDecimal("1.05"));
                BigDecimal maxAllowedBalance = account.getBalance().multiply(new BigDecimal("2.07"));
                if (increasedAmount.compareTo(maxAllowedBalance) <= 0) {
                    account.setBalance(increasedAmount);
                } else if (account.getBalance().compareTo(maxAllowedBalance) < 0) {
                    account.setBalance(maxAllowedBalance);
                }
                accountRepository.save(account);
            });
            log.info("Балансы счетов успешно обновлены");
        } catch (Exception e) {
            log.error("Ошибка при обновлении баланса счетов", e);
        }
    }
}
