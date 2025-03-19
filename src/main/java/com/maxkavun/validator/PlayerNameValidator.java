package com.maxkavun.validator;

import java.util.regex.Pattern;

public class PlayerNameValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{3,30}$");

    public boolean isInvalid(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return true;
        }
        return !NAME_PATTERN.matcher(playerName).matches();
    }

    public boolean areNamesSame(String player1Name, String player2Name) {
        return player1Name.equalsIgnoreCase(player2Name);
    }
}
