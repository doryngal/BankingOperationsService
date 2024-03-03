package com.example.BankingOperationsService.config;

import com.example.BankingOperationsService.model.Account;
import com.example.BankingOperationsService.model.User;
import com.example.BankingOperationsService.repository.AccountRepository;
import com.example.BankingOperationsService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Убедитесь, что таблицы пользователей и счетов пусты перед инициализацией
        if (userRepository.count() == 0 && accountRepository.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                User user = new User();
                user.setFullName("User " + i);
                user.setUsername("user" + i);
                user.setPassword(passwordEncoder.encode("password" + i));
                user.setBirthDate(LocalDate.of(1990 + i, Month.JANUARY, i));
                userRepository.save(user);

                Account account = new Account();
                account.setBalance(new BigDecimal(1000.00 * i).multiply(BigDecimal.valueOf(i)));
                account.setUser(user);
                accountRepository.save(account);
            }
        }
    }
}

