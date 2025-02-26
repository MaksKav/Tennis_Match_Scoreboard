package com.maxkavun.servlet;

import com.maxkavun.dto.MatchIdDto;
import com.maxkavun.exception.MatchNotFoundException;
import com.maxkavun.exception.PlayerServiceException;
import com.maxkavun.service.NewPlayersService;
import com.maxkavun.service.OngoingMatchesService;
import com.maxkavun.util.ResponseUtil;
import com.maxkavun.validator.PlayerNameValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;


@Slf4j
@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private final NewPlayersService newMatchService = new NewPlayersService();
    private final OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();
    private final PlayerNameValidator playerNameValidator = new PlayerNameValidator();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String player1Name = request.getParameter("player1");
        String player2Name = request.getParameter("player2");

        if (playerNameValidator.isInvalid(player1Name)) {
            log.error("Invalid player1 name: {}", player1Name);
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid player1 name");
            return;
        }

        if (playerNameValidator.isInvalid(player2Name)) {
            log.error("Invalid player2 name: {}", player2Name);
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid player2 name");
            return;
        }

        if (playerNameValidator.areNamesSame(player1Name, player2Name)) {
            log.error("Player 1 and Player 2 are same");
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Players names must be different");
            return;
        }
        try {
            UUID matchId = UUID.randomUUID();
            newMatchService.createPlayersIfNotExists(player1Name, player2Name);
            ongoingMatchesService.createMatch(matchId , player1Name, player2Name);

            ResponseUtil.sendResponse(response , HttpServletResponse.SC_OK, new MatchIdDto(matchId));

        } catch (PlayerServiceException exception) {
            log.error("Error processing new match request", exception);
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the match");
        } catch (MatchNotFoundException exception){
            log.error("Match for players {} and {} not found" , player1Name, player2Name);
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_NOT_FOUND, "Match for players " + player1Name + " and " + player2Name + " not found");
        }
    }
}
