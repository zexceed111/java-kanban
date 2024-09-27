import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.HttpTaskServer;
import main.httphandlers.HttpTestUtils;
import main.httphandlers.LocalDateTimeTypeAdapter;
import main.models.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;


public class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;

    @BeforeEach
    public void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.main();
    }

    @AfterEach
    public void destroy() {
        httpTaskServer.stop();
    }

    private Gson getDefaultGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }


    @Test
    void addTasksReturnCode200() throws IOException, InterruptedException {
        String uri = "Фhttp://localhost:8080/tasks";
        String jsonBody = "{\n" + "\t\"title\": \"Задача 5\",\n" + "\t\"description\": \"Описание задачи 4\",\n" + "\t\"id\": 60,\n" + "\t\"status\": \"NEW\",\n" + "\t\"duration\": 25,\n" + "\t\"startTime\": \"08.03.2024 13:00\"\n" + "}";

        HttpResponse<String> response = HttpTestUtils.sendPostRequest(uri, jsonBody);

        int expectedResponseCode = 201;
        int actualResponseCode = response.statusCode();
        List<Task> taskList = httpTaskServer.taskManager.getTasks();

        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(4, taskList.size(), "Не верное количество задач");
    }

    @Test
    void AddIntersectedTasksReturnCode406() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").POST(HttpRequest.BodyPublishers.ofString("{\n" + "\t\t\"title\": \"Задача 5\",\n" + "\t\t\"description\": \"Описание задачи 4\",\n" + "\t\t\"id\": 60,\n" + "\t\t\"status\": \"NEW\",\n" + "\t\t\"duration\": 30,\n" + "\t\t\"startTime\": \"28.03.2024 13:00\"\n" + "\t}")).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 406;
        int actualResponseCode = response.statusCode();
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
    }


    @Test
    void UpdateTasksReturnCode200() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").POST(HttpRequest.BodyPublishers.ofString("{\n" + "\t\t\"title\": \"Задача 1\",\n" + "\t\t\"description\": \"Описание задачи 1\",\n" + "\t\t\"id\": 1,\n" + "\t\t\"status\": \"NEW\",\n" + "\t\t\"duration\": 30,\n" + "\t\t\"startTime\": \"01.03.2024 13:00\"\n" + "\t}")).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 201;
        int actualResponseCode = response.statusCode();
        List<Task> taskList = httpTaskServer.taskManager.getTasks();
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(3, taskList.size(), "Не верное количество задач");
    }

    // получение всех задач
    @Test
    void CheckGetTasksReturnCode200AndTasksCountEqualToThree() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        List<Task> taskList = getDefaultGson().fromJson(response.body(), new TaskListTypeToken().getType());
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(3, taskList.size(), "Не верное количество задач");
    }

    // получение одной задачи с id 1
    @Test
    void CheckGetTasksWithId1ReturnCode200AndActualTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        Task expected = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW, Duration.ofMinutes(30), null);
        Task actual = getDefaultGson().fromJson(response.body(), Task.class);
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(expected, actual, "Задачи не совпали");
    }

    // получение одной задачи с неправильным id 100
    @Test
    void CheckGetTasksWithNonExistId100ReturnCode404() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/100");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 404;
        int actualResponseCode = response.statusCode();
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
    }

    // получение всех подзадач
    @Test
    void CheckGetSubtasksReturnCode200AndSubtasksCountEqualToSix() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        List<Subtask> taskList = getDefaultGson().fromJson(response.body(), new TaskListTypeToken().getType());
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(6, taskList.size(), "Не верное количество задач");
    }

    // получение одной подзадачи с id 7
    @Test
    void CheckGetSubtasksWithId7ReturnCode200AndActualTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/7");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        Subtask expected = new Subtask(7, "Подзадача 1", "Описание подзадачи 1", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024, 3, 28, 13, 0), 4);
        Subtask actual = getDefaultGson().fromJson(response.body(), Subtask.class);
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(expected, actual, "Задачи не совпали");
    }

    // получение одной подзадачи с неправильным id 100
    @Test
    void CheckGetSubTasksWithNonExistId100ReturnCode404() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/100");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 404;
        int actualResponseCode = response.statusCode();
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
    }

    // получение всех эпиков
    @Test
    void CheckGetEpicsReturnCode200AndEpicsCountEqualToFour() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        List<Epic> taskList = getDefaultGson().fromJson(response.body(), new TaskListTypeToken().getType());
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(4, taskList.size(), "Не верное количество задач");
    }

    // получение одного эпика с id 13
    @Test
    void CheckGetEpicWithId13ReturnCode200AndActualTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/13");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        Epic expected = new Epic(13, "Эпик 4", "Описание Эпика 4", Status.NEW, Duration.ofMinutes(0), null, new ArrayList<>());
        Epic actual = getDefaultGson().fromJson(response.body(), Epic.class);
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(expected, actual, "Задачи не совпали");
    }


    // получение одной эпика с неправильным id 100
    @Test
    void CheckGetEpicWithNonExistId100ReturnCode404() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/100");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 404;
        int actualResponseCode = response.statusCode();
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
    }

    @Test
    void CheckSendRequestBadURIReturnCode400() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/blelel");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 404;
        int actualResponseCode = response.statusCode();
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
    }

    @Test
    void CheckGetHistorySize() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        List<Task> taskList = getDefaultGson().fromJson(response.body(), new TaskListTypeToken().getType());
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(6, taskList.size(), "Не верное количество задач");
    }

    @Test
    void CheckGetPriorityFirstTaskId2() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        List<Task> taskList = getDefaultGson().fromJson(response.body(), new TaskListTypeToken().getType());
        Task expected = new Task(2, "Задача 2", "Описание задачи 2", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024, 3, 26, 11, 0));
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(expected, taskList.get(0), "Разные задачи");
    }

    @Test
    void CheckGetPriorityLastTaskHasId12() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Accept", "application/json;charset=UTF-8").GET().build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        List<Task> taskTreeSet = getDefaultGson().fromJson(response.body(), new TaskListTypeToken().getType());
        int expected = 12;
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(expected, taskTreeSet.get(7).getId(), "Разные задачи");
    }

}


class TaskListTypeToken extends TypeToken<List<Task>> {
    // здесь ничего не нужно реализовывать
}


