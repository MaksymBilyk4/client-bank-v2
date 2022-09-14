package com.clientbank.max.dao;

import com.clientbank.max.entities.Account;
import com.clientbank.max.entities.Customer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Repository
public class AccountDao implements Dao<Account> {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<Account> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityGraph entityGraph = entityManager.getEntityGraph("account_entity_graph");
        List<Account> accounts = entityManager.createQuery("SELECT a FROM Account a")
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
        entityManager.close();
        return accounts;
    }

    @Override
    public Account getOne(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            EntityGraph entityGraph = entityManager.getEntityGraph("account_entity_graph");
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.fetchgraph", entityGraph);
            return entityManager.find(Account.class, id, properties);
        } catch (HibernateException he) {
            log.error("Can`t get account with id = {} ", id, he);
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public Account save(Account account) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        account.setNumber(UUID.randomUUID().toString());

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(account);
            entityManager.getTransaction().commit();
            entityManager.close();
            return account;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t persist account: ", he);
            return null;
        }
    }

    @Override
    public void saveAll(List<Account> accounts) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(accounts);
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t persist accounts list: ", he);
        }
    }

    @Override
    public void deleteAll(List<Account> accounts) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("DELETE FROM Account a WHERE a IN (:account)")
                    .setParameter("account", accounts);
            query.executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove accounts list: ", he);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public boolean delete(Account account) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(account);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove account: ", he);
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
        Account account = entityManager.find(Account.class, id);

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(account);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove account with id = {} ", id, he);
            return false;
        }
    }

    public Customer createAccount(Long customerId, Account account) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityGraph entityGraph = entityManager.createEntityGraph("customer_entity_graph");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        Customer accOwner = entityManager.find(Customer.class, customerId, properties);
        account.setNumber(UUID.randomUUID().toString());
        account.setCustomer(accOwner);

        try {
            entityManager.getTransaction().begin();
            accOwner.getAccounts().add(account);
            entityManager.merge(account);
            entityManager.getTransaction().commit();
            entityManager.close();

            return accOwner;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t create account for customer with id = {} ", customerId, he);
            return null;
        } finally {
            entityManager.close();
        }
    }

    public boolean deleteAccount(String number, Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Customer customer = entityManager.find(Customer.class, id);
        Account account = null;

        try {
            for (Account a : customer.getAccounts()) {
                if (a.getNumber().equals(number)) {
                    account = a;
                }
            }
            customer.getAccounts().remove(account);

            entityManager.getTransaction().begin();
            entityManager.remove(account);
            entityManager.getTransaction().commit();
            entityManager.close();

            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove account with number = {} ", number, he);
            return false;
        }
    }

    public boolean toUpAccount(String number, Double amount) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT a FROM Account a WHERE a.number = :number")
                .setParameter("number", number);
        Account account = (Account) query.getSingleResult();

        try {
            entityManager.getTransaction().begin();
            account.setBalance(account.getBalance() + amount);
            entityManager.merge(account);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t up account with number = {} ", number, he);
            return false;
        } finally {
            entityManager.close();
        }
    }

    public boolean withdrawMoney(String number, Double amount) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT a FROM Account a WHERE a.number = :number")
                .setParameter("number", number);
        Account account = (Account) query.getSingleResult();

        try {
            if (account.getBalance() >= amount) {
                entityManager.getTransaction().begin();
                account.setBalance(account.getBalance() - amount);
                entityManager.merge(account);
                entityManager.getTransaction().commit();
                entityManager.close();
                return true;
            } else throw new IllegalArgumentException("You have not enough money on your bank account");
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t withdraw money from account with number = {} ", number, he);
            return false;
        } finally {
            entityManager.close();
        }
    }

    public boolean transferMoney(String from, String to, Double amount) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query fromQuery = entityManager.createQuery("SELECT a FROM Account a WHERE a.number = :number")
                .setParameter("number", from);
        Query toQuery = entityManager.createQuery("SELECT a FROM Account a WHERE a.number = :number")
                .setParameter("number", to);

        Account fromAccount = (Account) fromQuery.getSingleResult();
        Account toAccount = (Account) toQuery.getSingleResult();

        try {
            if (fromAccount.getBalance() >= amount) {
                entityManager.getTransaction().begin();
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);
                entityManager.merge(fromAccount);
                entityManager.merge(toAccount);
                entityManager.getTransaction().commit();
                entityManager.close();
                return true;
            } else
                throw new IllegalArgumentException("Account with number " + from + " has not enough money on your bank account");
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t transfer money from = {}, to = {}", from, to, he);
            return false;
        } finally {
            entityManager.close();
        }
    }
}
