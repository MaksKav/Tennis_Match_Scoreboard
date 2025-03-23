package com.maxkavun.model;
import lombok.*;
import java.util.UUID;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
public class MatchModel {
    private final UUID matchId ;
    private final String player1Name ;
    private final String player2Name ;
    private final Long player1Id;
    private final Long player2Id;
    private final MatchScoreModel score = new MatchScoreModel();
    private long startTime;

}
