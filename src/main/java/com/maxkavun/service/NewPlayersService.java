package com.maxkavun.service;

import com.maxkavun.dao.PlayerDao;
import com.maxkavun.exception.PlayerPersistenceException;
import com.maxkavun.exception.PlayerServiceException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class NewPlayersService {
    private final PlayerDao playerDao = new PlayerDao();

    public void createPlayersIfNotExists(String player1Name , String player2Name) {
        try {
            createPlayerIfNotExists(player1Name);
            createPlayerIfNotExists(player2Name);
            log.debug("Players created successfully in service");
        }catch (PlayerPersistenceException exception){
            throw new PlayerServiceException("Error while creating players: " + player1Name + " and " + player2Name);
        }
    }

    private void createPlayerIfNotExists(String playerName) {
        if (playerDao.findByName(playerName).isEmpty()) {
            playerDao.save(playerName);
        }
    }
}
