package com.maxkavun.service;

import com.maxkavun.dao.MatchDao;
import com.maxkavun.dao.PlayerDao;
import com.maxkavun.dto.FinishedMatchDto;
import com.maxkavun.dto.FinishedMatchResponse;
import com.maxkavun.entity.MatchEntity;
import com.maxkavun.entity.PlayerEntity;
import com.maxkavun.exception.FinishedMatchServiceException;
import com.maxkavun.exception.MatchPersistenceException;
import com.maxkavun.exception.PlayerPersistenceException;
import com.maxkavun.model.MatchModel;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FinishedMatchService {

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
                log.error("Error finding player for match: {}", matchModel, exception); // FIXME прокинуть исключение и обработать в сервлете
        } catch (MatchPersistenceException exception) {
            log.error("Error saving match: {}", matchModel, exception);
        }
    }

    public FinishedMatchResponse getFinishedMatches(int page, int size) {
        return getFinishedMatchesByPlayerName(page, size, null);
    }

    public FinishedMatchResponse getFinishedMatchesByPlayerName(int page, int size, String playerName) {
        try {
            int total = matchDao.getTotalMatches(playerName);
            List<FinishedMatchDto>  dtos =  matchDao.getMatchesByParams(page, size, playerName).stream()
                    .map(matchEntity -> new FinishedMatchDto(
                            matchEntity.getPlayer1().getName(),
                            matchEntity.getPlayer2().getName(),
                            matchEntity.getWinner().getName()))
                    .toList();
            return new FinishedMatchResponse(dtos , total);
        } catch (MatchPersistenceException exception) {
            throw new FinishedMatchServiceException("Failed to get finished matches with name: " + playerName, exception);
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
