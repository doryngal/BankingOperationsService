package com.example.BankingOperationsService.service;

import com.example.BankingOperationsService.dto.LoginDto;
import com.example.BankingOperationsService.dto.MessageDto;
import com.example.BankingOperationsService.model.User;
import com.example.BankingOperationsService.repository.UserRepository;
import com.example.BankingOperationsService.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public ResponseEntity<MessageDto> register(User user) {
        log.info("Попытка зарегистрировать пользователя с именем пользователя: {}", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("Регистрация не удалась: Имя пользователя {} уже занято", user.getUsername());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageDto("Ошибка: Имя пользователя уже занято!"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("Пользователь успешно зарегистрирован с именем пользователя: {}", user.getUsername());

        return ResponseEntity
                .ok()
                .body(new MessageDto("Пользователь успешно зарегистрирован!"));
    }

    @Override
    public ResponseEntity<?> authenticate(LoginDto loginDto) {
        log.info("Попытка аутентификации пользователя с именем пользователя: {}", loginDto.getUsername());
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            log.info("Пользователь успешно прошел аутентификацию с именем пользователя: {}", loginDto.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            log.error("Аутентификация не удалась: Неверное имя пользователя или пароль для имени пользователя: {}", loginDto.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDto("Ошибка: Неверное имя пользователя или пароль"));
        } catch (AuthenticationException e) {
            log.error("Не удалось пройти аутентификацию для имени пользователя: {}", loginDto.getUsername(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDto("Ошибка: Аутентификация не удалась"));
        }
    }
}
