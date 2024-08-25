
import main.HistoryManager;
import main.Managers;
import main.TaskManager;
import main.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager inMemoryTaskManager;
    private static HistoryManager historyManager;

    @BeforeEach
    public void setUp() throws ManagerSaveException {
        inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1",  Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2",  Status.NEW);
        Task task3 = new Task("Задача 3", "Описание задачи 3", Status.NEW);

        inMemoryTaskManager.addTask(task1); //1
        inMemoryTaskManager.addTask(task2);//2
        inMemoryTaskManager.addTask(task3);//3

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1",  Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2",  Status.NEW, new ArrayList<>());
        Epic epic3 = new Epic("Эпик 3", "Описание Эпика 3", Status.NEW, new ArrayList<>());

        inMemoryTaskManager.addEpic(epic1);//4
        inMemoryTaskManager.addEpic(epic2);//5
        inMemoryTaskManager.addEpic(epic3);//6

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",  Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",  Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3",  Status.NEW, epic2.getId());
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4",  Status.NEW, epic2.getId());
        Subtask subtask5 = new Subtask("Подзадача 5", "Описание подзадачи 5",  Status.NEW, epic3.getId());
        Subtask subtask6 = new Subtask("Подзадача 6", "Описание подзадачи 6",  Status.NEW, epic3.getId());
        inMemoryTaskManager.addSubtask(subtask1);//7
        inMemoryTaskManager.addSubtask(subtask2);//8
        inMemoryTaskManager.addSubtask(subtask3);//9
        inMemoryTaskManager.addSubtask(subtask4);//10
        inMemoryTaskManager.addSubtask(subtask5);//11
        inMemoryTaskManager.addSubtask(subtask6);//12
    }

    @Test
    void add() throws ManagerSaveException  {
        Task t = inMemoryTaskManager.getTask(1);
        final List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void ShouldHistorySizeBeTheSameBecauseTheTaskHasSameId() throws ManagerSaveException  {
        Task t = inMemoryTaskManager.getTask(2);
        for (int i = 0; i < 9; i++) {
            t = inMemoryTaskManager.getTask(1);
        }
        final List<Task> history = inMemoryTaskManager.getHistory();
        int id = history.get(0).getId();
        assertEquals(id, 2, "Не верный идентификатор первого таска");
        assertEquals(2, history.size(), "Количество записей в истории больше 2");
    }

    @Test
    void CheckIfHistoryContainsTasksSubtasksAndEpics() throws ManagerSaveException {
        Task t = inMemoryTaskManager.getTask(1);
        Subtask subtask = inMemoryTaskManager.getSubtask(10);
        Epic epic = inMemoryTaskManager.getEpic(4);
        final List<Task> history = inMemoryTaskManager.getHistory();
        boolean containsEpics = false;
        boolean containsTasks = false;
        boolean containsSubtasks = false;
        boolean contains = false;
        for (Task x : history) {
            if (x instanceof Epic) {
                containsEpics = true;
            }
            if (x instanceof Subtask) {
                containsSubtasks = true;
            }
            if (x instanceof Task) {
                containsTasks = true;
            }
            contains = containsEpics && containsSubtasks && containsTasks;
            if (contains)
                break;
        }
        assertTrue(contains, "Задачи одного типа");
    }

    @Test
    void CheckIfHistoryDontHasNewTaskWhenTheTaskHasSameId() throws ManagerSaveException  {

        Task t1 = inMemoryTaskManager.getTask(1);
        Task t2 = inMemoryTaskManager.getTask(1);
        final List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }


    @Test
    void CheckIfHistoryDontAddNewTaskWhenTheTaskHasSameId() throws ManagerSaveException  {

        Task t1 = inMemoryTaskManager.getTask(1);
        Subtask subtask = inMemoryTaskManager.getSubtask(10);
        Epic epic = inMemoryTaskManager.getEpic(4);
        Task t2 = inMemoryTaskManager.getTask(1);
        final List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История не пустая.");
    }

    @Test
    void ShouldAddTaskToTheTail() throws ManagerSaveException {

        Task t1 = inMemoryTaskManager.getTask(1);
        Subtask subtask = inMemoryTaskManager.getSubtask(10);
        Epic epic = inMemoryTaskManager.getEpic(4);
        Task expected = inMemoryTaskManager.getTask(1);
        final List<Task> history = inMemoryTaskManager.getHistory();
        final Task actual = history.get(2);
        assertEquals(actual, expected, "конечный узел не совпадает");
    }

    @Test
    void ShouldAddTaskToTheHead()  throws ManagerSaveException {

        Task t1 = inMemoryTaskManager.getTask(1);
        Subtask subtask = inMemoryTaskManager.getSubtask(10);
        Epic epic = inMemoryTaskManager.getEpic(4);
        Task t2 = inMemoryTaskManager.getTask(1);
        final List<Task> history = inMemoryTaskManager.getHistory();
        final Task actual = history.get(0);
        assertEquals(actual, subtask, "начальный узел не совпадает");
    }

}