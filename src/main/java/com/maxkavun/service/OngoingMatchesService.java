package com.maxkavun.service;

import com.maxkavun.dao.PlayerDao;
import com.maxkavun.model.MatchModel;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class OngoingMatchesService {
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();
    private static final Map<UUID, MatchModel> ongoingMatches = new ConcurrentHashMap<>();
    private final PlayerDao playerDao = new PlayerDao();

    private OngoingMatchesService() {}

    public static OngoingMatchesService getInstance (){
        return INSTANCE;
    }

    public UUID getOrCreateMatchId(String player1Name, String player2Name) {
        return ongoingMatches.entrySet().stream()
                .filter(entry -> {
                    MatchModel match = entry.getValue();
                    return match.getPlayer1Name().equals(player1Name) &&
                           match.getPlayer2Name().equals(player2Name);
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(() -> createMatch(player1Name, player2Name));
    }

    private UUID createMatch(String player1Name, String player2Name) {
        UUID uuid = UUID.randomUUID();
        try {
            Long player1Id = playerDao.getIdByName(player1Name);
            Long player2Id = playerDao.getIdByName(player2Name);
            MatchModel matchModel = MatchModel.builder().matchId(uuid).player1Name(player1Name).player2Name(player2Name).player1Id(player1Id).player2Id(player2Id).build();
            log.info("Match created successfully: {}", matchModel);
            ongoingMatches.put(uuid, matchModel);
            log.info("Match added to ongoing matches successfully");
        } catch (Exception e) {                                             //TODO  придумать тут какое то кастомное исключение
            log.error("Error while creating match");
        }
        return uuid;
    }


    public Optional<MatchModel> getMatch(UUID matchId) {
        return Optional.ofNullable(ongoingMatches.get(matchId));
    }

    public void removeMatch(UUID matchId) {
        if (getMatch(matchId).isPresent()) {
            ongoingMatches.remove(matchId);
        }
    }

}
