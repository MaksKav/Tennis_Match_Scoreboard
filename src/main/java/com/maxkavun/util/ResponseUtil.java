package com.maxkavun.util;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class ResponseUtil {
    private static final Gson gson = GsonSingleton.INSTANCE.getGson();

    private ResponseUtil() {
    }

    public static void sendResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.getWriter().write(message);
    }

    public static void sendErrorResponse(HttpServletResponse response, int status, Object errorObject) throws IOException {
        String errorJson = gson.toJson(errorObject);
        response.setStatus(status);
        response.getWriter().write(errorJson);
        log.error("Error response for {}: {}", status, errorObject);
    }
}
