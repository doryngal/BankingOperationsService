package com.example.BankingOperationsService.service;

import com.example.BankingOperationsService.dto.ContactDto;
import com.example.BankingOperationsService.dto.MessageDto;
import com.example.BankingOperationsService.dto.SearchDto;
import com.example.BankingOperationsService.dto.UserDto;
import com.example.BankingOperationsService.model.Contact;
import com.example.BankingOperationsService.model.ContactType;
import com.example.BankingOperationsService.model.User;
import com.example.BankingOperationsService.repository.ContactRepository;
import com.example.BankingOperationsService.repository.UserRepository;
import com.example.BankingOperationsService.repository.specification.UserSpecifications;
import com.example.BankingOperationsService.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserDto getUserById(Long userId) {
        log.info("Получение пользователя с ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден");
                });

        return UserDto.toUserDto(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#root.target.getUserIdFromSecurityContext()")
    public ResponseEntity<MessageDto> addContact(ContactDto contactDto) {
        Long userId = getUserIdFromSecurityContext();

        log.info("Добавление контакта для пользователя с ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
                });

        if (contactRepository.existsByValueAndTypeAndUserIdNot(contactDto.getValue(), contactDto.getType(), userId)) {
            log.error("Контакт уже занят");
            return ResponseEntity.badRequest().body(new MessageDto("Контакт уже занят"));
        }

        Contact newContact = new Contact(user, contactDto.getType(), contactDto.getValue());
        user.getContacts().add(newContact);
        userRepository.save(user);
        log.info("Контакт успешно добавлен");

        return ResponseEntity.ok(new MessageDto("Контакт успешно добавлен."));
    }

    @Override
    @CacheEvict(value = "users", key = "#root.target.getUserIdFromSecurityContext()")
    public ResponseEntity<MessageDto> updateContact(Long contactId, ContactDto contactDto) {
        Long userId = getUserIdFromSecurityContext();

        log.info("Обновление контакта с ID: {} для пользователя с ID: {}", contactId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
                });

        Contact contactToUpdate = user.getContacts().stream()
                .filter(contact -> contact.getId().equals(contactId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Контакт не найден");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Контакт не найден");
                });

        if (!contactToUpdate.getValue().equals(contactDto.getValue()) &&
                contactRepository.existsByValueAndTypeAndUserIdNot(contactDto.getValue(), contactDto.getType(), userId)) {
            log.error("Контакт уже занят");
            return ResponseEntity.badRequest().body(new MessageDto("Контакт уже занят"));

        }

        contactToUpdate.setType(contactDto.getType());
        contactToUpdate.setValue(contactDto.getValue());
        contactRepository.save(contactToUpdate);
        log.info("Контакт успешно обновлен");

        return ResponseEntity.ok(new MessageDto("Контакт успешно обновлен."));
    }

    @Override
    @CacheEvict(value = "users", key = "#root.target.getUserIdFromSecurityContext()")
    public ResponseEntity<?> deleteUserContact(Long contactId) {
        log.info("Удаление контакта с ID: {}", contactId);
        Long userId = getUserIdFromSecurityContext();

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> {
                    log.error("Контакт не найден");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Контакт не найден");
                });

        if (!contact.getUser().getId().equals(userId)) {
            log.error("Попытка удаления контакта без прав доступа");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDto("У вас нет прав для удаления этого контакта"));
        }

        if (contactRepository.countByUserId(userId) <= 2 && (contact.getType() == ContactType.EMAIL || contact.getType() == ContactType.PHONE)) {
            log.error("Попытка удаления последнего контакта");
            return ResponseEntity.badRequest().body(new MessageDto("Нельзя удалить последний контакт"));
        }


        log.info("Контакт успешно удален");
        contactRepository.deleteById(contactId);
        return ResponseEntity.ok().body(new MessageDto("Контакт успешно удален"));
    }

    private Long getUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        throw new IllegalStateException("Пользователь не аутентифицирован");
    }

    @Override
    @Cacheable(value = "usersByBirthDate", key = "#date.toString()")
    public List<UserDto> findByBirthDateAfter(LocalDate date) {
        log.info("Поиск пользователей с датой рождения после: {}", date);
        return userRepository.findByBirthDateAfter(date)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "usersByPhone", key = "#phone")
    public UserDto findByPhone(String phone) {
        log.info("Поиск пользователей по телефону: {}", phone);
        return UserDto.toUserDto(userRepository.findByContactsValue(phone));
    }

    @Override
    @Cacheable(value = "usersByEmail", key = "#email")
    public UserDto findByEmail(String email) {
        log.info("Поиск пользователей по email: {}", email);
        return UserDto.toUserDto(userRepository.findByContactsValue(email));
    }
    @Override
    @Cacheable(value = "usersByFullName", key = "#fullName")
    public List<UserDto> findByFullName(String fullName) {
        log.info("Поиск пользователей по ФИО: {}", fullName);
        return userRepository.findByFullNameStartingWith(fullName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    private UserDto convertToDto(User user) {
        // Преобразование сущности User в UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setBirthDate(user.getBirthDate());
        // Дополните преобразование остальными полями
        return userDto;
    }
}
