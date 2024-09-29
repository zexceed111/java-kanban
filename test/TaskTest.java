import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import main.Task;
import main.HttpTaskServer;

public class TaskTest {

    private HttpRequest createPostRequest(String uri, String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void AddTasksReturnCode200() throws IOException, InterruptedException {
        String uri = "http://localhost:8080/tasks";
        String jsonBody = "{\n" +
                "\t\t\"title\": \"Задача 5\",\n" +
                "\t\t\"description\": \"Описание задачи 4\",\n" +
                "\t\t\"id\": 60,\n" +
                "\t\t\"status\": \"NEW\",\n" +
                "\t\t\"duration\": 25,\n" +
                "\t\t\"startTime\": \"08.03.2024 13:00\"\n" +
                "\t}";

        HttpRequest request = createPostRequest(uri, jsonBody);
        HttpResponse<String> response = sendRequest(request);

        int expectedResponseCode = 201;
        int actualResponseCode = response.statusCode();


        HttpTaskServer httpTaskServer = new HttpTaskServer();
        List<Task> taskList = httpTaskServer.getTaskManager().getTasks();

        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(4, taskList.size(), "Не верное количество задач");
    }
}