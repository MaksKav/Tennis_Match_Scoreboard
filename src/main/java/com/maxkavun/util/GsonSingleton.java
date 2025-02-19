package com.maxkavun.util;

import com.google.gson.Gson;

public enum GsonSingleton {

    INSTANCE;

    private final Gson gson;

    GsonSingleton() {
        this.gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }
}
