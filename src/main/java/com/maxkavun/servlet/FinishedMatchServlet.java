package com.maxkavun.servlet;

import com.maxkavun.dto.FinishedMatchResponse;
import com.maxkavun.exception.FinishedMatchServiceException;
import com.maxkavun.service.FinishedMatchService;
import com.maxkavun.util.ResponseUtil;
import com.maxkavun.validator.PlayerNameValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
@WebServlet("/api/matches")
public class FinishedMatchServlet extends HttpServlet {

    FinishedMatchService finishedMatchService = new FinishedMatchService();
    PlayerNameValidator nameValidator = new PlayerNameValidator();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = Integer.parseInt(request.getParameter("page"));
        int size = Integer.parseInt(request.getParameter("size"));
        String playerName = request.getParameter("filter_by_player_name");
        log.info("Get parameters, page: {}, size: {} , playerName: {} ", page, size, playerName);

        try {
            FinishedMatchResponse finishedMatchResponse;
            if (playerName == null || playerName.isEmpty()) {
                finishedMatchResponse = finishedMatchService.getFinishedMatches(page, size);
                log.info("Successfully got finished matches list");
                ResponseUtil.sendResponse(response , HttpServletResponse.SC_OK , finishedMatchResponse);
            } else {
                if (nameValidator.isInvalid(playerName)) {
                    log.warn("Player name is not valid : {}", playerName);
                    ResponseUtil.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid player name: " + playerName);
                    return;
                }
                finishedMatchResponse = finishedMatchService.getFinishedMatchesByPlayerName(page , size , playerName);
                log.info("Successfully got finished matches list with playerName : {}", playerName);
                ResponseUtil.sendResponse(response, HttpServletResponse.SC_OK , finishedMatchResponse);
            }
        } catch (FinishedMatchServiceException exception){
            log.error("Error occurred while getting finished matches list", exception);
            ResponseUtil.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
