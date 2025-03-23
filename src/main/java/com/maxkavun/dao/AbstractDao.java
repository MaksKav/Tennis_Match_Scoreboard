package com.maxkavun.dao;

import com.maxkavun.exception.AbstractDaoException;
import com.maxkavun.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public abstract class AbstractDao<T , E extends AbstractDaoException> implements SavableDao<T> {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final Class<T> entityClass;
    private final Class<E> exceptionClass;

    protected AbstractDao(Class<T> entityClass  , Class<E> exceptionClass) {
        this.entityClass = entityClass;
        this.exceptionClass = exceptionClass;
    }

    @Override
    public void save(T entity) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (HibernateException exception) {
            log.error("Error occurred while saving ", exception);

            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw createException("Failed to save entity", exception);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private E createException(String message, Throwable cause) {
        try {
            return exceptionClass.getConstructor(String.class, Throwable.class).newInstance(message, cause);
        } catch (Exception e) {
            throw new RuntimeException("Error creating exception instance", e);
        }
    }
}
