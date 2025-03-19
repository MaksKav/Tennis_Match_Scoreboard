package com.maxkavun.service;

import com.maxkavun.dao.PlayerDao;
import com.maxkavun.entity.PlayerEntity;
import com.maxkavun.exception.PlayerPersistenceException;
import com.maxkavun.exception.PlayerServiceException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class NewPlayerService {
    private final PlayerDao playerDao = new PlayerDao();

    public void createPlayersIfNotExists(String player1Name , String player2Name) {
        try {
            player1Name = formatName(player1Name);
            player2Name = formatName(player2Name);

            createPlayerIfNotExists(player1Name);
            createPlayerIfNotExists(player2Name);
            log.info("Players created successfully in service");
        }catch (PlayerPersistenceException exception){
            throw new PlayerServiceException("Error while creating players: " + player1Name + " and " + player2Name);
        }
    }

    private void createPlayerIfNotExists(String playerName) {
        if (playerDao.findByName(playerName).isEmpty()) {
            playerDao.save(PlayerEntity.builder().name(playerName).build());
        }
    }

    private String formatName(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
