package com.maxkavun.service;

import com.maxkavun.dao.PlayerDao;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class NewMatchService {
    private final PlayerDao playerDao = new PlayerDao();

    public void createPlayersIfNotExists(String player1Name , String player2Name) {
        createPlayerIfNotExists(player1Name);
        createPlayerIfNotExists(player2Name);
        log.debug("Players created successfully in service");
    }

    private void createPlayerIfNotExists(String playerName) {
        if (playerDao.findByName(playerName).isEmpty()) {
            playerDao.save(playerName);
        }
    }
}
