package com.example.BankingOperationsService.repository.specification;

import com.example.BankingOperationsService.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecifications {
    public static Specification<User> birthDateAfter(LocalDate date) {
        return (root, query, cb) -> cb.greaterThan(root.get("birthDate"), date);
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, cb) -> cb.equal(root.get("phone"), phone);
    }

    public static Specification<User> fullNameLike(String fullName) {
        return (root, query, cb) -> cb.like(root.get("fullName"), fullName + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> cb.equal(root.get("email"), email);
    }
}

