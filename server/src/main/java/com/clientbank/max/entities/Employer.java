package com.clientbank.max.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "employers")
public class Employer extends AbstractEntity {
    private String name;
    private String address;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id")
//    private Set<Customer> customers;
}
