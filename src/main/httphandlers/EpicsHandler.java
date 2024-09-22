package main.httphandlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.TaskManager;
import main.models.Epic;
import main.models.ManagerSaveException;
import main.models.Subtask;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.*;

public class EpicsHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
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
                handleGetEpics(exchange);
                System.out.println("Get epics");
                break;
            }
            case GET_ONE: {
                handleGetEpic(exchange);
                System.out.println("Get one epic");
                break;
            }
            case GET_EPIC_SUBTASKS: {
                handleGetEpicSubtasks(exchange);
                System.out.println("Get epics subtasks");
                break;
            }
            case ADD: {
                System.out.println("try add epic");
                handleAddEpic(exchange);
                break;
            }
            case DELETE: {
                System.out.println("try delete epic");
                handleDeleteEpic(exchange);
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

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        Gson gson = getDefaultGson();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        String response = gson.toJson(taskManager.getEpics());
        writeResponse(exchange, response, HTTP_OK);

    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int epicId = Integer.parseInt(pathParts[2]);
        Gson gson = getDefaultGson();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        Epic epic = taskManager.getEpic(epicId);
        if (epic != null) {
            String response = gson.toJson(epic);
            writeResponse(exchange, response, HTTP_OK);
        } else {
            writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int epicId = Integer.parseInt(pathParts[2]);
        Gson gson = getDefaultGson();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        Epic epic = taskManager.getEpic(epicId);
        if (epic != null) {
            List<Subtask> subtasks = taskManager.getSubtasks().stream().filter(subtask -> epic.getSubtasksIds().contains(subtask.getId())).collect(Collectors.toList());
            String response = gson.toJson(subtasks);
            writeResponse(exchange, response, HTTP_OK);
        } else {
            writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }
    }

    private void handleAddEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(body);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Gson gson = getDefaultGson();
        Epic epic = gson.fromJson(jsonObject, Epic.class);
        System.out.println("Эпик " + epic.toString());

        try {
            System.out.println("Эпик " + epic.toString());
            taskManager.addEpic(epic);
            writeResponse(exchange, "Added", HTTP_CREATED);
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
        int id = Integer.parseInt(params.get("id"));
        try {
            taskManager.deleteEpic(id);
            writeResponse(exchange, "Deleted", HTTP_CREATED);
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
            writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }

    }

    private Gson getDefaultGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
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
        switch (requestMethod) {
            case "GET": {
                if (Pattern.matches("^/epics$", requestURI)) return Endpoint.GET_COLLECTION;
                if (Pattern.matches("^/epics/\\d+$", requestURI)) return Endpoint.GET_ONE;
                if (Pattern.matches("^/epics/\\d+/subtasks$", requestURI)) return Endpoint.GET_EPIC_SUBTASKS;
                return Endpoint.UNKNOWN;
            }
            case "POST": {
                if (Pattern.matches("^/epics$", requestURI)) return Endpoint.ADD;
                return Endpoint.UNKNOWN;
            }
            case "DELETE": {
                if (Pattern.matches("^/epics\\?id=\\d+$", requestURI)) return Endpoint.DELETE;
                return Endpoint.UNKNOWN;
            }
            default:
                return Endpoint.UNKNOWN;

        }
    }
}
