package main.httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    private static final Gson gson = createGson();

    private static Gson createGson() {
        return new GsonBuilder()
                .serializeNulls() // или другие настройки
                .create();
    }

    public static Gson getGson() {
        return gson;
    }
}