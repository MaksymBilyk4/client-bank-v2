package com.clientbank.max.dao;

import com.clientbank.max.entities.Customer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.List;

@Slf4j
@Repository
public class CustomerDao implements Dao<Customer> {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
//    @EntityGraph("customersFull")
    public List<Customer> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Customer> customers = entityManager.createQuery("SELECT c FROM Customer c").getResultList();
        entityManager.close();
        return customers;
    }

    @Override
    public Customer getOne(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try{
            return entityManager.find(Customer.class, id);
        }catch (HibernateException he) {
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

        try{
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

        try{
            entityManager.getTransaction().begin();
            entityManager.remove(customer);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        }catch (HibernateException he) {
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
        }catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove customer with id = {} ", id, he);
            return false;
        }
    }

    public Customer update (Customer customer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Customer updCustomer = entityManager.find(Customer.class, customer.getId());
        try {
            entityManager.getTransaction().begin();
            updCustomer.setName(customer.getName());
            updCustomer.setEmail(customer.getEmail());
            updCustomer.setAge(customer.getAge());
            updCustomer.setAccounts(customer.getAccounts());
            entityManager.merge(updCustomer);
            entityManager.getTransaction().commit();
            return updCustomer;
        }catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t update customer with id = {} ", customer.getId(), he);
            return null;
        } finally {
            entityManager.close();
        }
    }
}
