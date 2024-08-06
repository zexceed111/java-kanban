package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.HistoryManager;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Status;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;


import java.util.List;

public class Main {

    public static void main(String[] args) {


       // testTasks();
        testEpicAndSubtask();

    }

    private static void testTasks() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        System.out.println("Тест 1: Пустой список");
        List<Task> tasks = taskManager.getTasks();
        System.out.println("Таски должны быть пустые: " + tasks.isEmpty());
        System.out.println();

        System.out.println("Тест 2: Создание таски");
        Task task1 = new Task("таск.Имя", "таск.Описание", Status.NEW);
        Task task1Created = taskManager.createTask(task1);
        System.out.println("Созданная таска должна содержать айди: " + (task1Created.getId() != null));
        System.out.println("Список тасок должен содержать нашу таску: " + taskManager.getTasks());
        System.out.println();

        System.out.println("Тест 3: Обновление таски");
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        System.out.println("Обновленная таска должна иметь поля: " + taskManager.getTasks());
        System.out.println();

        System.out.println("Тест 3.1: История просмотров");
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("Тест 4: Получение таски по Id");
        System.out.println(taskManager.getByTaskId(task1.getId()));

        System.out.println("Тест 5: Удаление таски");
        boolean deleteRes = taskManager.deleteTask(task1.getId());
        System.out.println("Удаление должно пройти успешно: " + deleteRes);
        System.out.println("Список тасок пусой: " + taskManager.getTasks());
        System.out.println();

    }

    private static void testEpicAndSubtask() {
        TaskManager taskManager = Managers.getDefault();

        System.out.println("Тест 5: Пустой список эпика");
        List<Epic> epics = taskManager.getEpics();
        System.out.println("Эпик должны быть пуст: " + epics.isEmpty());
        System.out.println();

        System.out.println("Тест 6: Создание эпика1");
        Epic epic1 = new Epic("Поход в горы", "обязательно с друзьями");
        Epic epic1Created = taskManager.createEpic(epic1);
        System.out.println("Создали эпик эпик без подзадач: " + taskManager.getEpics());
        System.out.println();

        System.out.println("Тест 6.1: История просмотров");
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("Тест 7: Создание двух сабтасок для эпика1"); // epic1Created.getId()
        Subtask subtask1ForEpic1 = new Subtask(epic1Created.getId(), "Купить: ", "пластик. посуду ", Status.NEW);
        taskManager.createSubtask(subtask1ForEpic1);
        System.out.println("Пепечень сабтасок для эпика1: " + taskManager.getSubtasks());
        Subtask subtask2ForEpic1 = new Subtask(epic1Created.getId(), "Не забыть: ", "палатку, пенки", Status.NEW);
        taskManager.createSubtask(subtask2ForEpic1);
        System.out.println("Печать сабтасок для эпика1: " + taskManager.getSubtasks());
        System.out.println();

        System.out.println("Тест 7.1: История просмотров");
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("Тест 8: изменение статуса для сабтаск1, эпика1");
        subtask1ForEpic1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1ForEpic1);
        System.out.println("Обновленный список тасок: " + taskManager.getSubtasks());
        System.out.println("Обновленный статус эпика1: " + taskManager.getEpics());
        System.out.println();

        System.out.println("Тест 9: изменение статуса для сабтаск2, эпика1");
        subtask2ForEpic1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2ForEpic1);
        System.out.println("Обновленный список тасок: " + taskManager.getSubtasks());
        System.out.println("Обновленный статус эпика1: " + taskManager.getEpics());
        System.out.println();

        System.out.println("Тест 9.1: История просмотров");
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("Тест 10: удаление сабтаск1 из эпика1");
        System.out.println("Удаление сабтаск1: " + taskManager.deleteSubtask(subtask1ForEpic1.getId()));
        System.out.println("Обновленный список сабтасок: " + taskManager.getSubtasks());
        System.out.println("Обновленный статус эпика1: " + taskManager.getEpics());
        System.out.println();

        System.out.println("Тест 11: удаление сабтаск1 и его сабтасок");
        System.out.println("Удалить эпик1: " + taskManager.deleteEpic(epic1.getId()));
        System.out.println("Обновленный список тасок: " + taskManager.getSubtasks());
        System.out.println("Обновленный список эпика: " + taskManager.getEpics());

        System.out.println("Тест 12: Полное удаление");
        taskManager.deleteAllTasks();
        System.out.println("Проверяем список сабтасок: " + taskManager.getSubtasks());
        System.out.println("Проверяем список эпиков: " + taskManager.getEpics());
        System.out.println("Проверяем список тасок: " + taskManager.getTasks());


    }

}
