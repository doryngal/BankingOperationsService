package com.example.BankingOperationsService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account extends BaseEntity{
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToOne(mappedBy = "account")
    @JsonBackReference
    private User user;

    public Account(User user, BigDecimal balance) {
        this.user = user;
        this.balance = balance;
    }

    public Account() {

    }
}
