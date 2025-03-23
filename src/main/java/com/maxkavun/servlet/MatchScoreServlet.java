package com.maxkavun.servlet;

import com.google.gson.Gson;
import com.maxkavun.exception.MatchNotFoundException;
import com.maxkavun.model.MatchModel;
import com.maxkavun.model.request.MatchScoreRequest;
import com.maxkavun.service.FinishedMatchService;
import com.maxkavun.service.StandardMatchScoreCalculationService;
import com.maxkavun.service.OngoingMatchService;
import com.maxkavun.util.GsonSingleton;
import com.maxkavun.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/api/match-score")
public class MatchScoreServlet extends HttpServlet {

    private final OngoingMatchService ongoingMatchesService = OngoingMatchService.getInstance();
    private final StandardMatchScoreCalculationService matchScoreCalculationService = new StandardMatchScoreCalculationService();
    private final FinishedMatchService finishedMatchesService = new FinishedMatchService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String matchUUID = request.getParameter("uuid");
        log.info("Match ID: {}", matchUUID);
        if (matchUUID == null) {
            log.error("UUID is null");
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required parameter 'uuid'");
            return;
        }

        try {
            UUID matchId = UUID.fromString(matchUUID);
            MatchModel matchModel = findMatchById(matchId);
            log.info("Successfully retrieved match model: {}", matchModel);
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_OK, matchModel);

        } catch (MatchNotFoundException e) {
            log.error("Match not found: {}", e.getMessage());
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());

        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", matchUUID, e);
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        Gson gson = GsonSingleton.INSTANCE.getGson();
        MatchScoreRequest matchRequest = gson.fromJson(requestBody.toString(), MatchScoreRequest.class);

        String playerNumberParam = matchRequest.playerNumber();
        UUID matchId = UUID.fromString(matchRequest.matchUuid());

        if (isPlayerNumberInvalid(playerNumberParam)) {
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid player number");
            return;
        }


        try {
            MatchModel matchModel = findMatchById(matchId);
            switch (playerNumberParam) {
                case "1" -> matchScoreCalculationService.addPointToPlayer1(matchModel.getScore());
                case "2" -> matchScoreCalculationService.addPointToPlayer2(matchModel.getScore());
            }

            if (matchModel.getScore().isMatchFinished()) {
                finishedMatchesService.saveMatch(matchModel);
                ongoingMatchesService.removeMatch(matchModel.getMatchId());
            }

            ResponseUtil.sendResponse(response, HttpServletResponse.SC_OK, matchModel);
        } catch (MatchNotFoundException e) {
            log.error("Match with UUID: {} NOT FOUND", matchId);
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }


    private MatchModel findMatchById(UUID matchId) throws MatchNotFoundException {
        return ongoingMatchesService.getMatch(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match with UUID: " + matchId + " NOT FOUND"));
    }


    private boolean isPlayerNumberInvalid(String playerNumber) {
        return playerNumber == null || (!playerNumber.equals("1") && !playerNumber.equals("2"));
    }

}
