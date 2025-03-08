package com.maxkavun.service;

import com.maxkavun.dao.MatchDao;
import com.maxkavun.dao.PlayerDao;
import com.maxkavun.entity.MatchEntity;
import com.maxkavun.entity.PlayerEntity;
import com.maxkavun.exception.MatchPersistenceException;
import com.maxkavun.exception.PlayerPersistenceException;
import com.maxkavun.model.MatchModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class FinishedMatchesPersistenceService {
    PlayerDao playerDao = new PlayerDao();
    MatchDao matchDao = new MatchDao();

    public void saveMatch(MatchModel matchModel) {
        try {
            log.info("Saving match for players: {} and {}", matchModel.getPlayer1Name(), matchModel.getPlayer2Name());
            PlayerEntity player1 = getPlayerByName(matchModel.getPlayer1Name());
            PlayerEntity player2 = getPlayerByName(matchModel.getPlayer2Name());

            MatchEntity matchEntity = createMatchEntity(matchModel, player1, player2);
            matchDao.save(matchEntity);
            log.info("Match saved successfully with winner: {}", matchModel.getScore().getWinner() == 1 ? player1.getName() : player2.getName());

        } catch (PlayerPersistenceException exception) {
            log.error("Error finding player for match: {}", matchModel, exception);
        } catch (MatchPersistenceException exception) {
            log.error("Error saving match: {}", matchModel, exception);
        }
    }

    private PlayerEntity getPlayerByName(String playerName) {
        Optional<PlayerEntity> playerOptional = playerDao.findByName(playerName);
        return playerOptional.orElseThrow(() -> new PlayerPersistenceException("Player with name not found:  " + playerName));
    }

    private MatchEntity createMatchEntity(MatchModel matchModel, PlayerEntity firstPlayer, PlayerEntity secondPlayer) {
        int firstPlayerNumber = 1;
        return MatchEntity.builder()
                .player1(firstPlayer)
                .player2(secondPlayer)
                .winner(matchModel.getScore().getWinner() == firstPlayerNumber ? firstPlayer : secondPlayer)
                .build();
    }
}
