package com.maxkavun.dao;

import com.maxkavun.entity.MatchEntity;
import com.maxkavun.exception.MatchPersistenceException;
import com.maxkavun.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

@Slf4j
public class MatchDao extends AbstractDao <MatchEntity , MatchPersistenceException> {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public MatchDao() {
        super(MatchEntity.class , MatchPersistenceException.class);
    }

    public List<MatchEntity> getMatchesByParams(int page, int size) {
        List<MatchEntity> matches = Collections.emptyList();
        try (Session session = sessionFactory.openSession()){
            Query<MatchEntity> query = session.createQuery("FROM MatchEntity" , MatchEntity.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            matches = query.list();
            return matches;
        }catch (HibernateException exception){
            log.error("Error fetching matches for page {} with size {}", page, size, exception);
            throw  new MatchPersistenceException("Failed to fetch matches from database", exception);
        }
    }
}
