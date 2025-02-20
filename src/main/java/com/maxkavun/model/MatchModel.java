package com.maxkavun.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MatchModel {
    private final UUID matchId =  UUID.randomUUID();
    private final int player1Id;
    private final int player2Id;
    private final MatchScoreModel score = new MatchScoreModel();
}
