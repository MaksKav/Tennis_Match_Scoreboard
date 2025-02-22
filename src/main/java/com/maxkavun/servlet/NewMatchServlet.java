package com.maxkavun.servlet;

import com.maxkavun.service.NewMatchService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private final NewMatchService newMatchService = new NewMatchService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/new-match.html").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO вынести назначение контент типа в фильтр
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String player1Name = request.getParameter("player1");
        String player2Name = request.getParameter("player2");
        //TODO добавить валидацию имен
        newMatchService.createPlayersIfNotExists(player1Name , player2Name);


    }
}
