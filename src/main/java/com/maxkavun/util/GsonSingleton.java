package com.maxkavun.util;

import com.google.gson.Gson;
import lombok.Getter;

@Getter
public enum GsonSingleton {
    INSTANCE;

    private final Gson gson;

    GsonSingleton() {
        this.gson = new Gson();
    }

}
