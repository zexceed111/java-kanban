package main;

import main.models.Status;
import main.models.Task;
import main.models.Subtask;
import main.models.Epic;
import main.models.ManagerSaveException;


import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static Scanner scanner;
    public static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) throws ManagerSaveException {
        scanner = new Scanner(System.in);
        System.out.println("Поехали!");

        Task task1 = new Task("Задача 1", "Описание задачи 1",  Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        Task task3 = new Task("Задача 3", "Описание задачи 3",  Status.NEW);
        taskManager.addTask(task1);

        taskManager.addTask(task2);
        taskManager.addTask(task3);


        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1",  Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2",  Status.NEW, new ArrayList<>());
        Epic epic3 = new Epic("Эпик 3", "Описание Эпика 3",  Status.NEW, new ArrayList<>());

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",  Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",  Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3",  Status.NEW, epic2.getId());
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4",  Status.NEW, epic2.getId());
        Subtask subtask5 = new Subtask("Подзадача 5", "Описание подзадачи 5",  Status.NEW, epic3.getId());
        Subtask subtask6 = new Subtask("Подзадача 6", "Описание подзадачи 6",  Status.NEW, epic3.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);
        taskManager.addSubtask(subtask5);
        taskManager.addSubtask(subtask6);

        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(3));

        task1.setTitle("Задача 111");
        System.out.println(taskManager.getTask(1));
        task1.setTitle("Задача 11111");
        System.out.println(taskManager.getTask(1));
        task1.setTitle("Задача 111111");
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(4));
        System.out.println(taskManager.getEpic(5));
        System.out.println(taskManager.getEpic(6));
        System.out.println(taskManager.getEpic(4));
        System.out.println(taskManager.getEpic(5));

        printAllTasks(taskManager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);

        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    public static void printMainMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Работа с задачами");
        System.out.println("2 - Работа с эпиками");
        System.out.println("3 - Работа с подзадачами");
        System.out.println("4 - Выход");
    }
}
