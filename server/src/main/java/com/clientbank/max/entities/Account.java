package com.clientbank.max.entities;

import com.clientbank.max.enumable.Currency;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "accounts")
public class Account extends AbstractEntity{
    private String number;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    public Account(Currency currency, Double balance, Customer customer) {
        this.currency = currency;
        this.balance = balance;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                ", customer=" + customer.getId() +
                '}';
    }

}
