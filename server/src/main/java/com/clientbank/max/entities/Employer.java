package com.clientbank.max.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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
//    private Set<Customer> customers;
}
