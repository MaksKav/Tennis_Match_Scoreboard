package com.maxkavun.service;

import com.maxkavun.model.MatchScoreModel;

public interface MatchScoreCalculationService {

    void addPointToPlayer1(MatchScoreModel scoreModel);
    void addPointToPlayer2(MatchScoreModel scoreModel);
}
