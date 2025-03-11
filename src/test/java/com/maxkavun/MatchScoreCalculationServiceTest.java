package com.maxkavun;

import com.maxkavun.model.MatchScoreModel;
import com.maxkavun.service.StandardMatchScoreCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatchScoreCalculationServiceTest {
    private StandardMatchScoreCalculationService service;

    @BeforeEach
    void setUp() {
        service = new StandardMatchScoreCalculationService();
    }

    @Test
    void initialScoreTest() {
        // Arrange && Act
        MatchScoreModel scoreModel = new MatchScoreModel();

        // Assert
        assertAll("Initial score should be zero for all fields",
                () -> assertEquals("0", scoreModel.getPlayer1GameScore()),
                () -> assertEquals("0", scoreModel.getPlayer2GameScore()),
                () -> assertEquals(0, scoreModel.getPlayer1GamesInSet()),
                () -> assertEquals(0, scoreModel.getPlayer2GamesInSet()),
                () -> assertEquals(0, scoreModel.getPlayer1Sets()),
                () -> assertEquals(0, scoreModel.getPlayer2Sets()),
                () -> assertEquals(0, scoreModel.getWinner()),
                () -> assertFalse(scoreModel.isTiebreak()));
    }


    @Test
    void gameNotWonAfterFirstAdvantageTest() {
        // Arrange
        MatchScoreModel scoreModel = new MatchScoreModel();
        scoreModel.setPlayer1GameScore("40");
        scoreModel.setPlayer2GameScore("40");

        // Act
        service.addPointToPlayer1(scoreModel);

        // Assert
        assertAll("Game score should reflect advantage after first point",
                () -> assertEquals("AD", scoreModel.getPlayer1GameScore(), "Player1 should have advantage"),
                () -> assertEquals("40", scoreModel.getPlayer2GameScore(), "Player2 haven't change score"),
                () -> assertEquals(0, scoreModel.getPlayer1GamesInSet(), "Player1 games in set must to be not changed"),
                () -> assertEquals(0, scoreModel.getPlayer2GamesInSet(), "Player2 games in set must to be not changed")
        );
    }

    @Test
    void playerWinsGameInSetAfterScore40_0Test() {
        // Arrange
        MatchScoreModel scoreModel = new MatchScoreModel();
        scoreModel.setPlayer1GameScore("40");
        scoreModel.setPlayer2GameScore("0");

        // Act
        service.addPointToPlayer1(scoreModel);

        // Assert
        assertAll("Test if Player 1 wins the game after reaching score 40-0",
                () -> assertEquals(1, scoreModel.getPlayer1GamesInSet(), "Player1 gamesInSetScore must be increase 1"),
                () -> assertEquals(0, scoreModel.getPlayer2GamesInSet(), "Player2 gamesInSetScore must be not changed"),
                () -> assertEquals("0", scoreModel.getPlayer1GameScore(), "Player1 gameScore must be changed to initial value"),
                () -> assertEquals("0", scoreModel.getPlayer2GameScore(), "Player2 gameScore must be changed to initial value (or didn't change if it was zero)")
        );
    }

    @Test
    void afterGameInSetScore6_5StartsTiebreakTest() {
        // Arrange
        MatchScoreModel scoreModel = new MatchScoreModel();
        scoreModel.setPlayer1GamesInSet(6);
        scoreModel.setPlayer2GamesInSet(5);
        scoreModel.setPlayer2GameScore("40");

        // Act
        service.addPointToPlayer2(scoreModel);
        service.addPointToPlayer1(scoreModel);

        // Assert
        assertAll("If players have gamesInSetScore 6-5 and player2 won the game  then they play tiebreak",
                () -> assertTrue(scoreModel.isTiebreak()),
                () -> assertEquals("1", scoreModel.getPlayer1GameScore(), "When it's tiebreak players have another gamePoint score")
        );
    }

    @Test
    void tiebreakNotOverIfScoreInGameHaveDifferentOnePointTest() {
        // Arrange
        MatchScoreModel scoreModel = new MatchScoreModel();
        scoreModel.setPlayer1GamesInSet(6);
        scoreModel.setPlayer2GamesInSet(6);
        scoreModel.setTiebreak(true);
        scoreModel.setPlayer1TiebreakPoints(6);
        scoreModel.setPlayer2TiebreakPoints(6);

        // Act
        service.addPointToPlayer1(scoreModel);

        // Assert
        assertAll("When players have equal points in tiebreak, the game should continue",
                () -> assertEquals(6, scoreModel.getPlayer1GamesInSet(), "Player 1's games in set should not change"),
                () -> assertEquals(6, scoreModel.getPlayer2GamesInSet(), "Player 2's games in set should not change"),
                () -> assertEquals(0, scoreModel.getPlayer1Sets(), "Player 1 should not win the set yet"),
                () -> assertEquals(0, scoreModel.getPlayer2Sets(), "Player 2 should not win the set yet"),
                () -> assertTrue(scoreModel.isTiebreak(), "The tiebreak should not be over in this situation")
        );
    }

    @Test
    void tiebreakIsOverTest() {
        // Arrange
        MatchScoreModel scoreModel = new MatchScoreModel();
        scoreModel.setPlayer1GamesInSet(6);
        scoreModel.setPlayer2GamesInSet(6);
        scoreModel.setTiebreak(true);
        scoreModel.setPlayer1TiebreakPoints(6);
        scoreModel.setPlayer2TiebreakPoints(5);

        // Act
        service.addPointToPlayer1(scoreModel);

        // Assert
        assertAll("The tiebrake should be over when player have 7 more gamePoints and his opponent have 2 more less gamePoints",
                () -> assertFalse(scoreModel.isTiebreak(), "The tiebreak should be over"),
                () -> assertEquals("0", scoreModel.getPlayer1GameScore(), "Player 1's gamePoints should be reset to initial value"),
                () -> assertEquals("0", scoreModel.getPlayer2GameScore(), "Player 2's gamePoints should be reset to initial value"),
                () -> assertEquals(0, scoreModel.getPlayer1GamesInSet(), "Player 1's games in set should be reset to initial value"),
                () -> assertEquals(0, scoreModel.getPlayer2GamesInSet(), "Player 2's games in set should be reset to initial value"),
                () -> assertEquals(1, scoreModel.getPlayer1Sets(), "Player 1's won sets should be increased")
        );
    }

    @Test
    void winSetTest() {
        // Arrange
        MatchScoreModel scoreModel = new MatchScoreModel();
        scoreModel.setPlayer1GamesInSet(5);
        scoreModel.setPlayer2GamesInSet(4);
        scoreModel.setPlayer1GameScore("40");
        scoreModel.setPlayer2GameScore("30");

        // Act
        service.addPointToPlayer1(scoreModel);

        // Assert
        assertAll("After Player 1 wins the last game, the games in set score should reset and Player 1's set count should increase",
                () -> assertEquals("0", scoreModel.getPlayer1GameScore(), "Player 1's gamePoints should be reset to initial value"),
                () -> assertEquals("0", scoreModel.getPlayer2GameScore(), "Player 2's gamePoints should be reset to initial value"),
                () -> assertEquals(0, scoreModel.getPlayer1GamesInSet(), "Player 1's games in set should be reset to initial value"),
                () -> assertEquals(0, scoreModel.getPlayer2GamesInSet(), "Player 2's games in set should be reset to initial value"),
                () -> assertEquals(1, scoreModel.getPlayer1Sets(), "Player 1's won sets should be increased")
        );
    }
}
