package com.example.BankingOperationsService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class Contact extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    private String value;

    public Contact(User user, ContactType type, String value) {
        this.user = user;
        this.type = type;
        this.value = value;
    }

    public Contact() {

    }
}