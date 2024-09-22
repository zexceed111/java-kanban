package main;

import com.sun.net.httpserver.HttpServer;
import main.httphandlers.*;
import main.models.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class HttpTaskServer {
    private static final int PORT = 8080;
    public TaskManager taskManager = Managers.getDefault();
    private HttpServer httpServer;

    public void main() throws IOException {
        setUp();
        createServer();
        start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void createServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager));
    }

    public void start() {

        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("HTTP-сервер завершен!");

    }

    public void setUp() throws ManagerSaveException {

        Duration duration30minutes = Duration.ofMinutes(30);
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, duration30minutes, null);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, duration30minutes, LocalDateTime.of(2024, 3, 26, 11, 0));
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW, duration30minutes, LocalDateTime.of(2024, 3, 28, 12, 0));
        taskManager.addTask(task1); //1
        taskManager.addTask(task2);//2
        taskManager.addTask(task3);//3
        LocalDateTime localEpicDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", Status.NEW, Duration.ofMinutes(0), localEpicDateTime, new ArrayList<>());
        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2", Status.NEW, Duration.ofMinutes(0), localEpicDateTime, new ArrayList<>());
        Epic epic3 = new Epic("Эпик 3", "Описание Эпика 3", Status.NEW, Duration.ofMinutes(0), localEpicDateTime, new ArrayList<>());
        taskManager.addEpic(epic1);//4
        taskManager.addEpic(epic2);//5
        taskManager.addEpic(epic3);//6
        LocalDateTime localSubtaskDateTime = LocalDateTime.of(2024, 3, 28, 13, 0);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, duration30minutes, localSubtaskDateTime, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, duration30minutes, localSubtaskDateTime.plusHours(1), epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Status.NEW, duration30minutes, localSubtaskDateTime.plusHours(2), epic2.getId());
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4", Status.NEW, duration30minutes, localSubtaskDateTime.plusHours(3), epic2.getId());
        Subtask subtask5 = new Subtask("Подзадача 5", "Описание подзадачи 5", Status.NEW, duration30minutes, localSubtaskDateTime.plusHours(4), epic3.getId());
        Subtask subtask6 = new Subtask("Подзадача 6", "Описание подзадачи 6", Status.NEW, duration30minutes, localSubtaskDateTime.plusHours(5), epic3.getId());
        taskManager.addSubtask(subtask1);//7
        taskManager.addSubtask(subtask2);//8
        taskManager.addSubtask(subtask3);//9
        taskManager.addSubtask(subtask4);//10
        taskManager.addSubtask(subtask5);//11
        taskManager.addSubtask(subtask6);//12
        Epic epic4 = new Epic("Эпик 4", "Описание Эпика 4", Status.NEW, Duration.ofMinutes(0), null, new ArrayList<>());
        taskManager.addEpic(epic4);//13
        taskManager.getTask(1);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getSubtask(7);
        taskManager.getSubtask(8);
        taskManager.getEpic(4);
        taskManager.getEpic(5);

    }
}
