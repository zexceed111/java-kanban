package main.httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import static java.net.HttpURLConnection.*;
public class PriorityHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public PriorityHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println(exchange.getRequestURI());
        Endpoint endpoint = getEndpoint(String.valueOf(exchange.getRequestURI()), exchange.getRequestMethod());
        System.out.println(endpoint.toString());

        switch (endpoint) {
            case GET_COLLECTION: {
                handleGetHistory(exchange);
                System.out.println("Get priority");
                break;
            }
            case UNKNOWN: {
                writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
                break;
            }
            default:
                writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        Gson gson = getDefaultGson();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        String response = gson.toJson(taskManager.getPrioritizedTasks());
        writeResponse(exchange, response, HTTP_OK);

    }

    private Gson getDefaultGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    private Endpoint getEndpoint(String requestURI, String requestMethod) {
        if (requestMethod.equals("GET")) {
            if (Pattern.matches("^/prioritized$", requestURI)) return Endpoint.GET_COLLECTION;
            return Endpoint.UNKNOWN;
        }
        return Endpoint.UNKNOWN;
    }
}

