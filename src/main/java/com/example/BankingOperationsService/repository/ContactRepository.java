package com.example.BankingOperationsService.repository;

import com.example.BankingOperationsService.model.Contact;
import com.example.BankingOperationsService.model.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT COUNT(c) > 0 FROM Contact c WHERE c.value = :value AND c.type = :type AND c.user.id <> :userId")
    Boolean existsByValueAndTypeAndUserIdNot(@Param("value") String value, @Param("type") ContactType type, @Param("userId") Long userId);

    Long countByUserId(Long userId);
}

