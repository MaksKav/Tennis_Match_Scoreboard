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
public class MatchDao extends AbstractDao<MatchEntity, MatchPersistenceException> {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public MatchDao() {
        super(MatchEntity.class, MatchPersistenceException.class);
    }


    /**
     * Retrieves a paginated list of matches, optionally filtered by player name.
     *
     * @param page       the page number (starting from 1)
     * @param size       the number of elements per page
     * @param playerName the name of the player to filter matches by, or {@code null} to get all matches
     * @return a list of matches, optionally filtered by player name, in which the player participated
     */
    public List<MatchEntity> getMatchesByParams(int page, int size, String playerName) {
        List<MatchEntity> matches = Collections.emptyList();
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM MatchEntity m";

            if (playerName != null) {
                hql += " WHERE m.player1.name = :playerName OR m.player2.name = :playerName";
            }

            Query<MatchEntity> query = session.createQuery(hql, MatchEntity.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            if (playerName != null) {
                query.setParameter("playerName", playerName);
            }

            matches = query.list();
            return matches;
        } catch (HibernateException exception) {
            log.error("Error fetching matches for page {} with size {}", page, size, exception);
            throw new MatchPersistenceException("Failed to fetch matches from database", exception);
        }
    }

    public int getTotalMatches(String playerName) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(m) FROM MatchEntity m";

            if (playerName != null) {
                hql += " WHERE m.player1.name = :playerName OR m.player2.name = :playerName";
            }

            Query<Long> query = session.createQuery(hql, Long.class);

            if (playerName != null) {
                query.setParameter("playerName", playerName);
            }
            return query.uniqueResult().intValue();
        } catch (Exception exception) {
            throw new MatchPersistenceException("Failed to get total matches" + (playerName != null ? " for player: " + playerName : ""), exception);
        }
    }

}
