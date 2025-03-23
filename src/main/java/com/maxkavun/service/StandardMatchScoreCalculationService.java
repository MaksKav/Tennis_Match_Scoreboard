package com.maxkavun.service;

import com.maxkavun.model.MatchScoreModel;

public class StandardMatchScoreCalculationService implements MatchScoreCalculationService {
    private static final String INITIAL_SCORE = "0";
    private static final String FIRST_POINT_SCORE = "15";
    private static final String SECOND_POINT_SCORE = "30";
    private static final String THIRD_POINT_SCORE = "40";
    private static final String ADVANTAGE_SCORE = "AD";
    private static final int GAMES_IN_SET_TO_WIN = 6;
    private static final int TIEBREAK_WIN_POINTS = 7;
    private static final int DIFFERENCE_TO_WIN = 2;
    private static final int INITIAL_VALUE = 0;
    private static final int ONE_POINT = 1 ;

    @Override
    public void addPointToPlayer1(MatchScoreModel scoreModel) {
        if (scoreModel.isTiebreak()) {
            handleTiebreakPoint(scoreModel, true);
        } else {
            handleRegularPoint(scoreModel, true);
        }
    }

    @Override
    public void addPointToPlayer2(MatchScoreModel scoreModel) {
        if (scoreModel.isTiebreak()) {
            handleTiebreakPoint(scoreModel, false);
        } else {
            handleRegularPoint(scoreModel, false);
        }
    }

    private void handleRegularPoint(MatchScoreModel scoreModel, boolean isPlayer1) {
        String player1Score = isPlayer1 ? scoreModel.getPlayer1GameScore() : scoreModel.getPlayer2GameScore();
        String player2Score = isPlayer1 ? scoreModel.getPlayer2GameScore() : scoreModel.getPlayer1GameScore();

        switch (player1Score) {
            case INITIAL_SCORE -> {
                if (isPlayer1) scoreModel.setPlayer1GameScore(FIRST_POINT_SCORE);
                else scoreModel.setPlayer2GameScore(FIRST_POINT_SCORE);
            }
            case FIRST_POINT_SCORE -> {
                if (isPlayer1) scoreModel.setPlayer1GameScore(SECOND_POINT_SCORE);
                else scoreModel.setPlayer2GameScore(SECOND_POINT_SCORE);
            }
            case SECOND_POINT_SCORE -> {
                if (isPlayer1) scoreModel.setPlayer1GameScore(THIRD_POINT_SCORE);
                else scoreModel.setPlayer2GameScore(THIRD_POINT_SCORE);
            }
            case THIRD_POINT_SCORE -> {
                if (player2Score.equals(ADVANTAGE_SCORE)) {
                    scoreModel.setPlayer1GameScore(THIRD_POINT_SCORE);
                    scoreModel.setPlayer2GameScore(THIRD_POINT_SCORE);
                } else if (player2Score.equals(THIRD_POINT_SCORE)) {
                    if (isPlayer1) scoreModel.setPlayer1GameScore(ADVANTAGE_SCORE);
                    else scoreModel.setPlayer2GameScore(ADVANTAGE_SCORE);
                } else {
                    handleGameWin(scoreModel, isPlayer1);
                }
            }
            case ADVANTAGE_SCORE -> handleGameWin(scoreModel, isPlayer1);
        }
    }

    private void handleGameWin(MatchScoreModel scoreModel, boolean isPlayer1) {
        scoreModel.setPlayer1GameScore(INITIAL_SCORE);
        scoreModel.setPlayer2GameScore(INITIAL_SCORE);

        if (isPlayer1) {
            scoreModel.setPlayer1GamesInSet(scoreModel.getPlayer1GamesInSet() + ONE_POINT);
        } else {
            scoreModel.setPlayer2GamesInSet(scoreModel.getPlayer2GamesInSet() + ONE_POINT);
        }

        checkSetEnd(scoreModel);
    }

    private void handleTiebreakPoint(MatchScoreModel scoreModel, boolean isPlayer1) {
        if (isPlayer1) {
            scoreModel.setPlayer1TiebreakPoints(scoreModel.getPlayer1TiebreakPoints() + ONE_POINT);
            String player1GameScore = String.valueOf(scoreModel.getPlayer1TiebreakPoints());
            scoreModel.setPlayer1GameScore(player1GameScore);
        } else {
            scoreModel.setPlayer2TiebreakPoints(scoreModel.getPlayer2TiebreakPoints() + ONE_POINT);
            String player2GameScore = String.valueOf(scoreModel.getPlayer2TiebreakPoints());
            scoreModel.setPlayer2GameScore(player2GameScore);
        }

        if (scoreModel.getPlayer1TiebreakPoints() >= TIEBREAK_WIN_POINTS && scoreModel.getPlayer1TiebreakPoints() >= scoreModel.getPlayer2TiebreakPoints() + DIFFERENCE_TO_WIN) {
            scoreModel.setPlayer1Sets(scoreModel.getPlayer1Sets() + ONE_POINT);
            resetScoreAfterTiebreak(scoreModel);
        } else if (scoreModel.getPlayer2TiebreakPoints() >= TIEBREAK_WIN_POINTS && scoreModel.getPlayer2TiebreakPoints() >= scoreModel.getPlayer1TiebreakPoints() + DIFFERENCE_TO_WIN) {
            scoreModel.setPlayer2Sets(scoreModel.getPlayer2Sets() + ONE_POINT);
            resetScoreAfterTiebreak(scoreModel);
        }

        if(scoreModel.isMatchFinished()){
            setWinnerIfMatchFinished(scoreModel);
        }
    }

    private void checkSetEnd(MatchScoreModel scoreModel) {
        if (scoreModel.getPlayer1GamesInSet() == GAMES_IN_SET_TO_WIN && scoreModel.getPlayer2GamesInSet() == GAMES_IN_SET_TO_WIN) {
            scoreModel.setTiebreak(true);
            return;
        }

        if (scoreModel.getPlayer1GamesInSet() >= GAMES_IN_SET_TO_WIN && scoreModel.getPlayer1GamesInSet() >= scoreModel.getPlayer2GamesInSet() + DIFFERENCE_TO_WIN) {
            scoreModel.setPlayer1Sets(scoreModel.getPlayer1Sets() + ONE_POINT);
            resetScoreAfterTiebreak(scoreModel);
        } else if (scoreModel.getPlayer2GamesInSet() >= GAMES_IN_SET_TO_WIN && scoreModel.getPlayer2GamesInSet() >= scoreModel.getPlayer1GamesInSet() + DIFFERENCE_TO_WIN) {
            scoreModel.setPlayer2Sets(scoreModel.getPlayer2Sets() + ONE_POINT);
            resetScoreAfterTiebreak(scoreModel);
        }

        if (scoreModel.isMatchFinished()){
            setWinnerIfMatchFinished(scoreModel);
        }
    }

    private void setWinnerIfMatchFinished(MatchScoreModel scoreModel){
        if (scoreModel.getPlayer1Sets() >= 2) {
            int firstPlayer = 1 ;
            scoreModel.setWinner(firstPlayer);
        } else if (scoreModel.getPlayer2Sets() >= 2) {
            int secondPlayer = 2 ;
            scoreModel.setWinner(secondPlayer);
        }
    }

    private void resetScoreAfterTiebreak(MatchScoreModel scoreModel) {
        scoreModel.setTiebreak(false);
        scoreModel.setPlayer1GamesInSet(INITIAL_VALUE);
        scoreModel.setPlayer2GamesInSet(INITIAL_VALUE);
        scoreModel.setPlayer1TiebreakPoints(INITIAL_VALUE);
        scoreModel.setPlayer2TiebreakPoints(INITIAL_VALUE);
        scoreModel.setPlayer1GameScore(INITIAL_SCORE);
        scoreModel.setPlayer2GameScore(INITIAL_SCORE);
    }
}