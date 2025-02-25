package com.maxkavun.validator;

import java.util.regex.Pattern;

public class PlayerNameValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9]{3,30}$");

    public boolean isInvalid(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return false;
        }
        return NAME_PATTERN.matcher(playerName).matches();
    }

    public boolean areNamesDifferent(String player1Name, String player2Name) {
        return !player1Name.equals(player2Name);
    }
}
