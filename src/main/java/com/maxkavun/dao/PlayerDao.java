package com.maxkavun.dao;

import com.maxkavun.entity.PlayerEntity;
import com.maxkavun.exception.PlayerPersistenceException;
import com.maxkavun.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

@Slf4j
public class PlayerDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Optional<PlayerEntity> findByName(String name) {
        Optional<PlayerEntity> playerEntity = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            playerEntity = Optional.ofNullable(session
                    .createQuery("select p from PlayerEntity p where p.name = :name", PlayerEntity.class)
                    .setParameter("name", name)
                    .getSingleResultOrNull());
        } catch (HibernateException  exception) {
            log.error("Error occurred while fetching player with name: {}", name, exception);
            throw new PlayerPersistenceException("Failed to find PlayerEntity with name: " + name, exception);
        }
        return playerEntity;
    }


    public void save(String playerName) {
        Session session = null;
        try {
            PlayerEntity playerEntity = PlayerEntity.builder()
                    .name(playerName)
                    .build();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(playerEntity);
            session.getTransaction().commit();
        } catch (HibernateException exception) {
            log.error("Error occurred while saving player", exception);

            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new PlayerPersistenceException("Failed to save PlayerEntity", exception);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


}
