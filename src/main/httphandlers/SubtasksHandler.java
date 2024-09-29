package main.httphandlers;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.TaskManager;
import main.models.ManagerSaveException;
import main.models.Subtask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import static java.net.HttpURLConnection.*;

public class SubtasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Endpoint endpoint = getEndpoint(String.valueOf(exchange.getRequestURI()), exchange.getRequestMethod());
        System.out.println(endpoint.toString());

        switch (endpoint) {
            case GET_COLLECTION: {
                handleGetSubtasks(exchange);
                System.out.println("Get subtasks");
                break;
            }
            case GET_ONE: {
                handleGetSubtask(exchange);
                System.out.println("Get one subtask");
                break;
            }
            case ADD: {
                System.out.println("try add subtask");
                handleAddSubtask(exchange);
                break;
            }
            case DELETE: {
                System.out.println("try delete subtask");
                handleDeleteSubtask(exchange);
                break;
            }
            case UPDATE: {
                System.out.println("try update subtask");
                handleUpdateSubtask(exchange);
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

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        Gson gson = GsonProvider.getGson();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        String response = gson.toJson(taskManager.getSubtasks());
        writeResponse(exchange, response, HTTP_OK);

    }

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int subtaskId = Integer.parseInt(pathParts[2]);
        Gson gson = GsonProvider.getGson();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        Subtask subtask = taskManager.getSubtask(subtaskId);
        if (subtask != null) {
            String response = gson.toJson(subtask);
            writeResponse(exchange, response, HTTP_OK);
        } else {
            writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }
    }

    private void handleAddSubtask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(body);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Gson gson = GsonProvider.getGson();
        Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
        System.out.println("Подзадача" + subtask.toString());
        try {
            taskManager.addSubtask(subtask);
            writeResponse(exchange, "Added", HTTP_CREATED);
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
        int id = Integer.parseInt(params.get("id"));
        try {
            taskManager.deleteSubtask(id);
            writeResponse(exchange, "Deleted", HTTP_CREATED);
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
            writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }

    }

    private void handleUpdateSubtask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Gson gson = GsonProvider.getGson();
        Subtask subtask = gson.fromJson(jsonObject, Subtask.class);
        System.out.println(subtask.toString());
        try {
            taskManager.updateSubtask(subtask);
            writeResponse(exchange, "Updated", HTTP_CREATED);
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        }
    }



    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    private Endpoint getEndpoint(String requestURI, String requestMethod) {
        switch (requestMethod) {
            case "GET": {
                if (Pattern.matches("^/subtasks$", requestURI)) return Endpoint.GET_COLLECTION;
                if (Pattern.matches("^/subtasks/\\d+$", requestURI)) return Endpoint.GET_ONE;
                return Endpoint.UNKNOWN;
            }
            case "POST": {
                if (Pattern.matches("^/subtasks$", requestURI)) return Endpoint.ADD;
                if (Pattern.matches("^/subtasks\\?id=\\d+$", requestURI)) return Endpoint.UPDATE;
                return Endpoint.UNKNOWN;
            }
            case "DELETE": {
                if (Pattern.matches("^/subtasks\\?id=\\d+$", requestURI)) return Endpoint.DELETE;
                return Endpoint.UNKNOWN;
            }
            default:
                return Endpoint.UNKNOWN;

        }


    }
}



