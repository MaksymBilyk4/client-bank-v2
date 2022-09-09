package com.clientbank.max.dao;

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
public class EmployerDao implements Dao<Employer> {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<Employer> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityGraph entityGraph = entityManager.getEntityGraph("employer_entity_graph");
        List<Employer> employers = entityManager.createQuery("SELECT e FROM Employer e")
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
        entityManager.close();
        return employers;
    }

    @Override
    public Employer getOne(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            EntityGraph entityGraph = entityManager.getEntityGraph("employer_entity_graph");
            Employer employer = (Employer) entityManager.createQuery("SELECT e FROM Employer e WHERE e.id = :id")
                    .setParameter("id", id)
                    .setHint("javax.persistence.fetchgraph", entityGraph)
                    .getSingleResult();
            return employer;

        } catch (HibernateException he) {
            log.error("Can`t get employer with id = {} ", id, he);
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public Employer save(Employer employer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(employer);
            entityManager.getTransaction().commit();
            entityManager.close();
            return employer;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t persist employer: ", he);
            return null;
        }
    }

    @Override
    public void saveAll(List<Employer> employers) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(employers);
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t persist employers list: ", he);
        }
    }

    @Override
    public void deleteAll(List<Employer> employers) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("DELETE FROM Employer e WHERE e IN (:employer)")
                    .setParameter("employer", employers);
            query.executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove employers list: ", he);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public boolean delete(Employer employer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(employer);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove employer: ", he);
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
        Employer employer = entityManager.find(Employer.class, id);

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(employer);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (HibernateException he) {
            entityManager.getTransaction().rollback();
            log.error("Can`t remove employer with id = {} ", id, he);
            return false;
        }
    }
}
