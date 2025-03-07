package com.maxkavun.dao;

import com.maxkavun.entity.MatchEntity;
import com.maxkavun.exception.MatchPersistenceException;
import com.maxkavun.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class MatchDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void save(MatchEntity matchEntity) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(matchEntity);
            session.getTransaction().commit();
        }catch (HibernateException exception){
            log.error("Error occurred while saving match", exception);
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new MatchPersistenceException("Failed to save MatchEntity", exception);
        }finally {
            if (session != null){
                session.close();
            }
        }
    }
}
