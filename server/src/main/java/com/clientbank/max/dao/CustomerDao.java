package com.clientbank.max.dao;

import com.clientbank.max.entities.Customer;
import com.clientbank.max.entities.Employer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CustomerDao implements Dao<Customer> {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<Customer> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityGraph entityGraph = entityManager.getEntityGraph("customer_entity_graph");
        List<Customer> customers = entityManager.createQuery("SELECT c FROM Customer c")
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
        entityManager.close();
        return customers;
    }

    @Override
    public Customer getOne(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            EntityGraph entityGraph = entityManager.getEntityGraph("customer_entity_graph");
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.fetchgraph", entityGraph);
            return entityManager.find(Customer.class, id, properties);
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t get customer with id = {} ", id, he);
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public Customer save(Customer customer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(customer);
            entityManager.getTransaction().commit();
            entityManager.close();
            return customer;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t persist customer: ", he);
            return null;
        }
    }

    @Override
    public void saveAll(List<Customer> customers) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(customers);
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t persist customers list: ", he);
        }
    }

    @Override
    public void deleteAll(List<Customer> customers) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("DELETE FROM Customer e WHERE e IN (:customer)")
                    .setParameter("customer", customers);
            query.executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove customers list: ", he);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public boolean delete(Customer customer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(customer);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove customer: ", he);
            return false;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Customer customer = entityManager.find(Customer.class, id);

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(customer);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove customer with id = {} ", id, he);
            return false;
        }
    }

    public Customer update(Customer customer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityGraph entityGraph = entityManager.getEntityGraph("customer_entity_graph");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);

        Customer updCustomer = entityManager.find(Customer.class, customer.getId(), properties);

        try {
            entityManager.getTransaction().begin();
            updCustomer.setName(customer.getName());
            updCustomer.setEmail(customer.getEmail());
            updCustomer.setAge(customer.getAge());
            updCustomer.setAccounts(customer.getAccounts());
            entityManager.merge(updCustomer);
            entityManager.getTransaction().commit();
            return updCustomer;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t update customer with id = {} ", customer.getId(), he);
            return null;
        } finally {
            entityManager.close();
        }
    }

    public boolean addEmployer(Long customerId, Long employerId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();


//        EntityGraph customerEntityGraph = entityManager.getEntityGraph("customer_entity_graph");
//        Map<String, Object> customerProperties = new HashMap<>();
//        customerProperties.put("javax.persistence.fetchgraph", customerEntityGraph);
        Customer customer = entityManager.find(Customer.class, customerId);

//        EntityGraph employerEntityGraph = entityManager.getEntityGraph("employer_entity_graph");
//        Map<String, Object> employerProperties = new HashMap<>();
//        customerProperties.put("javax.persistence.fetchgraph", employerEntityGraph);
        Employer employer = entityManager.find(Employer.class, employerId);

        try {
            entityManager.getTransaction().begin();
            customer.getEmployers().add(employer);
            entityManager.merge(customer);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t add employer with id = {} to customer with id = {}", employerId, customerId, he);
            return false;
        } finally {
            entityManager.close();
        }
    }
}
