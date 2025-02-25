package com.maxkavun.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class MatchModel {
    private final UUID matchId ;
    private final Long player1Id;
    private final Long player2Id;
    private final MatchScoreModel score = new MatchScoreModel();
}
