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
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NamedEntityGraph(
        name = "account_entity_graph",
        attributeNodes = {
                @NamedAttributeNode(value = "customer", subgraph = "customerInAccount")
        },
        subgraphs = {
                @NamedSubgraph(name = "customerInAccount", attributeNodes = {
                        @NamedAttributeNode(value = "accounts")
                })
        }
)
@Table(name = "accounts")
public class Account extends AbstractEntity {
    private String number;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
//    @JsonIgnore
    private Customer customer;

//    public Account(String number, Currency currency, Double balance, Customer customer) {
//        this.number = number;
//        this.currency = currency;
//        this.balance = balance;
//        this.customer = customer;
//    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                ", customer_id=" + customer.getId() +
                '}';
    }

}
