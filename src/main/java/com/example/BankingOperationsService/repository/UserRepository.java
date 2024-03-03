package com.example.BankingOperationsService.repository;


import com.example.BankingOperationsService.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    List<User> findByBirthDateAfter(LocalDate date);;
    @Query("select u from User u join u.contacts c where c.type = 'PHONE' and c.value = :phone")
    User findByContactsValue(String phone);
    List<User> findByFullNameStartingWith(String fullName);
}
