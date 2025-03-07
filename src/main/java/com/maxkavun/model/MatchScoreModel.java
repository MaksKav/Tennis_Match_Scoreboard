package com.maxkavun.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MatchScoreModel {
    private static final String INITIAL_SCORE = "0";
    private static final int SETS_TO_WIN = 2;

    private String player1GameScore = INITIAL_SCORE;
    private String player2GameScore = INITIAL_SCORE;

    private int player1GamesInSet;
    private int player2GamesInSet;

    private int player1Sets;
    private int player2Sets;

    private boolean isTiebreak;

    private int player1TiebreakPoints;
    private int player2TiebreakPoints;
    private int winner;

    public boolean isMatchFinished() {
        return player1Sets == SETS_TO_WIN || player2Sets == SETS_TO_WIN;
    }
}
