package com.maxkavun.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Builder
@Getter
@AllArgsConstructor
public class MatchModel {
    private final UUID matchId ;
    private final String player1Name ;
    private final String player2Name ;
    private final Long player1Id;
    private final Long player2Id;
    private final MatchScoreModel score = new MatchScoreModel();
}
