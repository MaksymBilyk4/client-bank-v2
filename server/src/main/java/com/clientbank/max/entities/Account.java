package com.clientbank.max.entities;

import com.clientbank.max.enumable.Currency;
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
    private Currency currency;
    private Double balance;

//    @JoinColumn(name = "customer_id")
//    @OneToMany(fetch = FetchType.LAZY)
//    private Customer customer;
}
