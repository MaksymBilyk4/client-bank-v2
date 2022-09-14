package com.clientbank.max.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@NamedEntityGraph(
        name = "employer_entity_graph",
        attributeNodes = {
            @NamedAttributeNode(value = "customers", subgraph = "employersAndAccountsInCustomer")
        },
        // ИСПОЛЬЗУЮ ВТОРОЙ SUBGRAPH ("reverse") ПОТОМУ ЧТО ПРИ ЗАПРОСЕ
        // НА ПОЛУЧЕНИЕ ОДНОГО Employer ВОЗНИКАЕТ ОШИБКА LazyInitializationException
        // ХОТЯ БЕЗ SUBGRAPH ("reverse") ЗАПРОС НА ПОЛУЧЕНИЕ ВСЕХ Employer ОТРАБАТЫВАЕТ БЕЗ ОШИБОК
        subgraphs = {
                @NamedSubgraph(name = "employersAndAccountsInCustomer", attributeNodes = {
                        @NamedAttributeNode(value = "employers", subgraph = "reverse"),
                        @NamedAttributeNode("accounts"),
                }),
                @NamedSubgraph(name = "reverse", attributeNodes = {
                        @NamedAttributeNode(value = "customers"),
                }),
        }
)
public class Employer extends AbstractEntity {
    private String name;
    private String address;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToMany(mappedBy = "employers")
    private Set<Customer> customers;

    @Override
    public String toString() {
        return "Employer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", customers=" + customers +
                '}';
    }
}
