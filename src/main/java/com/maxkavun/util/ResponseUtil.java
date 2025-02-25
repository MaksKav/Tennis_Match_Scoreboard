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

    public static void sendResponse(HttpServletResponse response, int status, Object message) throws IOException {
        String json = gson.toJson(message);
        response.setStatus(status);
        response.getWriter().write(json);
    }
}
