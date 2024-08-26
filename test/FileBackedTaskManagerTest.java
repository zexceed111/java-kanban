import main.FileBackedTaskManager;
import main.InMemoryHistoryManager;
import main.Managers;
import main.TaskManager;
import main.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static main.FileBackedTaskManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FileBackedTaskManagerTest {
    private static TaskManager fileBackedTaskManager;


    @BeforeEach
    public void setUp() throws ManagerSaveException {
        fileBackedTaskManager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        fileBackedTaskManager.addTask(task1); //1
        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", Status.NEW, new ArrayList<>());
        fileBackedTaskManager.addEpic(epic1);//2
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, epic1.getId());
        fileBackedTaskManager.addSubtask(subtask1);//3
        fileBackedTaskManager.addSubtask(subtask2);//4
    }

    @Test
    public void saveSeveralTasksToTestFile() throws IOException {
        TaskManager fileBackedTaskManager1 = new FileBackedTaskManager(new InMemoryHistoryManager(), new File("test.csv"));
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        fileBackedTaskManager1.addTask(task1); //1
        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", Status.NEW, new ArrayList<>());
        fileBackedTaskManager1.addEpic(epic1);//2
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, epic1.getId());
        fileBackedTaskManager1.addSubtask(subtask1);//3
        fileBackedTaskManager1.addSubtask(subtask2);//4
        Task actual = fileBackedTaskManager1.getTask(1);
        Subtask subtask = fileBackedTaskManager1.getSubtask(3);
        TaskManager loadedFromFileBackedTaskManager = loadFromFile(new File("test.csv"));
        assertEquals(loadedFromFileBackedTaskManager.getTasks().size(), fileBackedTaskManager1.getTasks().size(), "Задачи не совпадают");
        assertEquals(loadedFromFileBackedTaskManager.getHistory().size(), fileBackedTaskManager1.getHistory().size(), "Задачи не совпадают");
    }

    @Test
    public void saveSeveralTasksToTestFileWithoutHistory() throws IOException {
        TaskManager fileBackedTaskManager1 = new FileBackedTaskManager(new InMemoryHistoryManager(), new File("test.csv"));
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        fileBackedTaskManager1.addTask(task1); //1
        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", Status.NEW, new ArrayList<>());
        fileBackedTaskManager1.addEpic(epic1);//2
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, epic1.getId());
        fileBackedTaskManager1.addSubtask(subtask1);//3
        fileBackedTaskManager1.addSubtask(subtask2);//4
        TaskManager loadedFromFileBackedTaskManager = loadFromFile(new File("test.csv"));
        assertEquals(loadedFromFileBackedTaskManager.getTasks().size(), fileBackedTaskManager1.getTasks().size(), "Задачи не совпадают");
        assertEquals(loadedFromFileBackedTaskManager.getHistory().size(), fileBackedTaskManager1.getHistory().size(), "Задачи не совпадают");
    }

    @Test
    public void getTaskById() throws ManagerSaveException {

        Task expected = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        Task actual = fileBackedTaskManager.getTask(1);
        assertEquals(expected, actual, "Задачи не совпадают");
    }

    @Test
    public void addTask() throws ManagerSaveException {
        int id = fileBackedTaskManager.getId() + 1;
        Task newTask = new Task(id, "Задача 1", "Описание задачи 1", Status.NEW);
        fileBackedTaskManager.addTask(newTask);
        Task actualTask = fileBackedTaskManager.getTask(id);
        assertEquals(newTask, actualTask, "Задачи не совпадают");
    }

    @Test
    public void updateTask() throws ManagerSaveException {
        Task newTask = new Task(1, "Задача 1. Обновленная", "Описание задачи 1", Status.NEW);
        fileBackedTaskManager.updateTask(newTask);
        Task actualTask = fileBackedTaskManager.getTask(1);
        assertEquals(newTask, actualTask, "Задачи не совпадают");
    }

    @Test
    public void deleteTask() throws ManagerSaveException {
        fileBackedTaskManager.deleteTask(1);
        Task expected = fileBackedTaskManager.getTask(1);
        assertNull(expected, "Задача не удалена.");
    }

    @Test
    public void deleteAllSubtasks() throws ManagerSaveException {
        fileBackedTaskManager.deleteSubtasks();
        assertEquals(0, fileBackedTaskManager.getSubtaskHashMap().size(), "Подзадачи не очищены");
    }

    @Test
    public void getSubtaskById() throws ManagerSaveException {
        Subtask expected = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2);
        Subtask actual = fileBackedTaskManager.getSubtask(3);
        assertEquals(expected, actual, "Подзадачи не совпадают");
    }

    @Test
    public void addSubtask() throws ManagerSaveException {
        int id = fileBackedTaskManager.getId() + 1;
        Subtask newSubtask = new Subtask(id, "Подзадача 7", "Описание подзадачи 7", Status.NEW, 4);
        fileBackedTaskManager.addSubtask(newSubtask);
        Subtask actualSubtask = fileBackedTaskManager.getSubtask(id);
        assertEquals(newSubtask, actualSubtask, "Задачи не совпадают");

    }

    @Test
    public void updateSubtask() throws ManagerSaveException {
        Subtask newSubtask = new Subtask(3, "Подзадача 3, Обновлена", "Описание подзадачи 3", Status.NEW, 2);
        fileBackedTaskManager.updateSubtask(newSubtask);
        Subtask actualSubtask = fileBackedTaskManager.getSubtask(3);
        assertEquals(newSubtask, actualSubtask, "Задачи не совпадают");
    }

    @Test
    public void deleteSubtask() throws ManagerSaveException {
        fileBackedTaskManager.deleteSubtask(7);
        Subtask expected = fileBackedTaskManager.getSubtask(1);
        assertNull(expected, "Подзадача не удалена.");
    }


    @Test
    public void addEpic() throws ManagerSaveException {
        int id = fileBackedTaskManager.getId() + 1;
        Epic newEpic = new Epic(id, "Эпик 4", "Описание Эпика 4", Status.NEW, new ArrayList<>());
        fileBackedTaskManager.addEpic(newEpic);
        Epic actualEpic = fileBackedTaskManager.getEpic(id);
        assertEquals(newEpic, actualEpic, "Эпики не совпадают");
    }

    @Test
    public void updateEpic() throws ManagerSaveException {
        ArrayList<Integer> subtasksIds = new ArrayList<>();
        subtasksIds.add(4);
        subtasksIds.add(5);
        Epic newEpic = new Epic(2, "Эпик 1. Обновлен", "Описание Эпика 1", Status.NEW, subtasksIds);
        fileBackedTaskManager.updateEpic(newEpic);
        Epic actualEpic = fileBackedTaskManager.getEpic(2);
        assertEquals(newEpic, actualEpic, "Эпики не совпадают");
    }

    @Test
    public void deleteEpic() throws ManagerSaveException {
        fileBackedTaskManager.deleteEpic(4);
        Epic expected = fileBackedTaskManager.getEpic(4);
        assertNull(expected, "Эпик удален.");
    }

}
