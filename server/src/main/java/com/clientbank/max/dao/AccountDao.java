package com.clientbank.max.dao;

import com.clientbank.max.entities.Account;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.List;

@Slf4j
@Repository
public class AccountDao implements Dao<Account> {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<Account> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Account> accounts = entityManager.createQuery("FROM Account e").getResultList();
        entityManager.close();
        return accounts;
    }

    @Override
    public Account getOne(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager.find(Account.class, id);
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
            Query query = entityManager.createQuery("DELETE FROM Account e WHERE e IN (:account)")
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
}
