package com.clientbank.max.entities;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "customers")
@NamedEntityGraph(
        name = "customer_entity_graph",
        attributeNodes = {
                @NamedAttributeNode("accounts"),
                @NamedAttributeNode(value = "employers", subgraph = "customerInEmployer")
        },
        subgraphs = {
                @NamedSubgraph(name = "customerInEmployer", attributeNodes = {
                    @NamedAttributeNode(value = "customers")
                })
        }
)
@Entity
public class Customer extends AbstractEntity {
    private String name;
    private String email;
    private Integer age;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "customers_employers",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "employer_id")
    )
    private Set<Employer> employers;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private Set<Account> accounts = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return name.equals(customer.name) && email.equals(customer.email) && age.equals(customer.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, age);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", employers=" + employers +
                ", accounts=" + accounts +
                '}';
    }

}
