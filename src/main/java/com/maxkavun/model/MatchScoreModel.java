package com.maxkavun.model;

public class MatchScoreModel {

    private static final String INITIAL_SCORE = "0";
    private static final String FIRST_POINT_SCORE = "15";
    private static final String SECOND_POINT_SCORE = "30";
    private static final String THIRD_POINT_SCORE = "40";
    private static final String ADVANTAGE_SCORE = "AD";

    private static final int GAMES_IN_SET_TO_WIN = 6;
    private static final int SETS_TO_WIN = 2;
    private static final int TIEBREAK_WIN_POINTS = 7;
    private static final int TIEBREAK_DIFFERENCE = 2;

    private String player1GameScore = INITIAL_SCORE;
    private String player2GameScore = INITIAL_SCORE;

    private int player1GamesInSet;
    private int player2GamesInSet;

    private int player1Sets;
    private int player2Sets;

    private boolean isTiebreak;

    private int player1TiebreakPoints;
    private int player2TiebreakPoints;

    public void addPointToPlayer1() {
        if (isTiebreak) {
            handleTiebreakPoint(true);
        } else {
            handleRegularPoint(true);
        }
    }

    public void addPointToPlayer2() {
        if (isTiebreak) {
            handleTiebreakPoint(false);
        } else {
            handleRegularPoint(false);
        }
    }

    private void handleRegularPoint(boolean isPlayer1) {
        String player1Score = isPlayer1 ? player1GameScore : player2GameScore;
        String player2Score = isPlayer1 ? player2GameScore : player1GameScore;

        switch (player1Score) {
            case INITIAL_SCORE -> {
                if (isPlayer1) player1GameScore = FIRST_POINT_SCORE;
                else player2GameScore = FIRST_POINT_SCORE;
            }
            case FIRST_POINT_SCORE -> {
                if (isPlayer1) player1GameScore = SECOND_POINT_SCORE;
                else player2GameScore = SECOND_POINT_SCORE;
            }
            case SECOND_POINT_SCORE -> {
                if (isPlayer1) player1GameScore = THIRD_POINT_SCORE;
                else player2GameScore = THIRD_POINT_SCORE;
            }
            case THIRD_POINT_SCORE -> {
                if (player2Score.equals(ADVANTAGE_SCORE)) {
                    // return to "40-40"
                    player1GameScore = THIRD_POINT_SCORE;
                    player2GameScore = THIRD_POINT_SCORE;
                } else if (player2Score.equals(THIRD_POINT_SCORE)) {
                    // Getting an advantage
                    if (isPlayer1) player1GameScore = ADVANTAGE_SCORE;
                    else player2GameScore = ADVANTAGE_SCORE;
                } else {
                    // win game
                    handleGameWin(isPlayer1);
                }
            }
            case ADVANTAGE_SCORE -> handleGameWin(isPlayer1);
        }
    }

    private void handleGameWin(boolean isPlayer1) {
        player1GameScore = INITIAL_SCORE;
        player2GameScore = INITIAL_SCORE;

        if (isPlayer1) {
            player1GamesInSet++;
        } else {
            player2GamesInSet++;
        }

        checkSetEnd();
    }

    private void checkSetEnd() {
        if (player1GamesInSet == GAMES_IN_SET_TO_WIN && player2GamesInSet == GAMES_IN_SET_TO_WIN) {
            isTiebreak = true;
            return;
        }

        if (player1GamesInSet >= GAMES_IN_SET_TO_WIN && player1GamesInSet >= player2GamesInSet + 2) {
            player1Sets++;
            resetSetScore();
        } else if (player2GamesInSet >= GAMES_IN_SET_TO_WIN && player2GamesInSet >= player1GamesInSet + 2) {
            player2Sets++;
            resetSetScore();
        }
    }

    private void handleTiebreakPoint(boolean isPlayer1) {
        if (isPlayer1) {
            player1TiebreakPoints++;
        } else {
            player2TiebreakPoints++;
        }

        // Check for victory in tiebreaker (up to 7 points with a difference of 2)
        if (player1TiebreakPoints >= TIEBREAK_WIN_POINTS && player1TiebreakPoints >= player2TiebreakPoints + TIEBREAK_DIFFERENCE) {
            player1Sets++;
            resetSetScore();
        } else if (player2TiebreakPoints >= TIEBREAK_WIN_POINTS && player2TiebreakPoints >= player1TiebreakPoints + TIEBREAK_DIFFERENCE) {
            player2Sets++;
            resetSetScore();
        }
    }

    private void resetSetScore() {
        player1GamesInSet = 0;
        player2GamesInSet = 0;
        player1TiebreakPoints = 0;
        player2TiebreakPoints = 0;
        isTiebreak = false;
    }

    public boolean isMatchFinished() {
        return player1Sets == SETS_TO_WIN || player2Sets == SETS_TO_WIN;
    }

    public int getWinner() {
        if (!isMatchFinished()) {
            throw new IllegalStateException("Match is not finished yet");
        }
        return player1Sets > player2Sets ? 1 : 2;
    }
}
