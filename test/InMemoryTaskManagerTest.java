import main.Managers;
import main.TaskManager;
import main.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager inMemoryTaskManager;

    @BeforeEach
    public void setUp() throws ManagerSaveException {
        inMemoryTaskManager = Managers.getDefaultInMemory();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW);

        inMemoryTaskManager.addTask(task1); //1
        inMemoryTaskManager.addTask(task2);//2
        inMemoryTaskManager.addTask(task3);//3

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2", Status.NEW, new ArrayList<>());
        Epic epic3 = new Epic("Эпик 3", "Описание Эпика 3", Status.NEW, new ArrayList<>());

        inMemoryTaskManager.addEpic(epic1);//4
        inMemoryTaskManager.addEpic(epic2);//5
        inMemoryTaskManager.addEpic(epic3);//6

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Status.NEW, epic2.getId());
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4", Status.NEW, epic2.getId());
        Subtask subtask5 = new Subtask("Подзадача 5", "Описание подзадачи 5", Status.NEW, epic3.getId());
        Subtask subtask6 = new Subtask("Подзадача 6", "Описание подзадачи 6", Status.NEW, epic3.getId());
        inMemoryTaskManager.addSubtask(subtask1);//7
        inMemoryTaskManager.addSubtask(subtask2);//8
        inMemoryTaskManager.addSubtask(subtask3);//9
        inMemoryTaskManager.addSubtask(subtask4);//10
        inMemoryTaskManager.addSubtask(subtask5);//11
        inMemoryTaskManager.addSubtask(subtask6);//12
    }

    @Test
    public void getNewId() {
        int id = inMemoryTaskManager.getNewId();
        assertEquals(id, 13, "Идентификатор некорректный");
    }

    @Test
    public void getTaskById() throws ManagerSaveException {
        Task expected = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        Task actual = inMemoryTaskManager.getTask(1);
        assertEquals(expected, actual, "Задачи не совпадают");
    }

    @Test
    public void addTask() throws ManagerSaveException {
        int id = inMemoryTaskManager.getId() + 1;
        Task newTask = new Task(id, "Задача 1", "Описание задачи 1", Status.NEW);
        inMemoryTaskManager.addTask(newTask);
        Task actualTask = inMemoryTaskManager.getTask(id);
        assertEquals(newTask, actualTask, "Задачи не совпадают");
    }

    @Test
    public void updateTask() throws ManagerSaveException {
        Task newTask = new Task(1, "Задача 1. Обновленная", "Описание задачи 1", Status.NEW);
        inMemoryTaskManager.updateTask(newTask);
        Task actualTask = inMemoryTaskManager.getTask(1);
        assertEquals(newTask, actualTask, "Задачи не совпадают");
    }

    @Test
    public void deleteTask() throws ManagerSaveException {
        inMemoryTaskManager.deleteTask(1);
        Task expected = inMemoryTaskManager.getTask(1);
        assertNull(expected, "Задача не удалена.");
    }

    @Test
    public void deleteAllSubtasks() throws ManagerSaveException {
        inMemoryTaskManager.deleteSubtasks();
        assertEquals(0, inMemoryTaskManager.getSubtaskHashMap().size(), "Подзадачи не очищены");
    }

    @Test
    public void getSubtaskById() throws ManagerSaveException {
        Subtask expected = new Subtask(7, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 4);
        Subtask actual = inMemoryTaskManager.getSubtask(7);
        assertEquals(expected, actual, "Подзадачи не совпадают");
    }

    @Test
    public void addSubtask() throws ManagerSaveException {
        int id = inMemoryTaskManager.getId() + 1;
        Subtask newSubtask = new Subtask(id, "Подзадача 7", "Описание подзадачи 7", Status.NEW, 4);
        inMemoryTaskManager.addSubtask(newSubtask);
        Subtask actualSubtask = inMemoryTaskManager.getSubtask(id);
        assertEquals(newSubtask, actualSubtask, "Задачи не совпадают");

    }

    @Test
    public void updateSubtask() throws ManagerSaveException {
        Subtask newSubtask = new Subtask(7, "Подзадача 1, Обновлена", "Описание подзадачи 7", Status.NEW, 4);
        inMemoryTaskManager.updateSubtask(newSubtask);
        Subtask actualSubtask = inMemoryTaskManager.getSubtask(7);
        assertEquals(newSubtask, actualSubtask, "Задачи не совпадают");
    }

    @Test
    public void deleteSubtask() throws ManagerSaveException {
        inMemoryTaskManager.deleteSubtask(7);
        Subtask expected = inMemoryTaskManager.getSubtask(1);
        assertNull(expected, "Подзадача не удалена.");
    }

    @Test
    public void getEpicById() throws ManagerSaveException {
        ArrayList<Integer> subtasksIds = new ArrayList<>();
        subtasksIds.add(4);
        subtasksIds.add(5);
        Epic expected = new Epic(4, "Эпик 1", "Описание Эпика 1", Status.NEW, subtasksIds);
        Epic actual = inMemoryTaskManager.getEpic(4);
        assertEquals(expected, actual, "Эпики не совпадают");
    }

    @Test
    public void addEpic() throws ManagerSaveException {
        int id = inMemoryTaskManager.getId() + 1;
        Epic newEpic = new Epic(id, "Эпик 4", "Описание Эпика 4", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.addEpic(newEpic);
        Epic actualEpic = inMemoryTaskManager.getEpic(id);
        assertEquals(newEpic, actualEpic, "Эпики не совпадают");
    }

    @Test
    public void updateEpic() throws ManagerSaveException {
        ArrayList<Integer> subtasksIds = new ArrayList<>();
        subtasksIds.add(4);
        subtasksIds.add(5);
        Epic newEpic = new Epic(4, "Эпик 1. Обновлен", "Описание Эпика 1", Status.NEW, subtasksIds);
        inMemoryTaskManager.updateEpic(newEpic);
        Epic actualEpic = inMemoryTaskManager.getEpic(4);
        assertEquals(newEpic, actualEpic, "Эпики не совпадают");
    }

    @Test
    public void deleteEpic() throws ManagerSaveException {
        inMemoryTaskManager.deleteEpic(4);
        Epic expected = inMemoryTaskManager.getEpic(4);
        assertNull(expected, "Эпик удален.");
    }
}