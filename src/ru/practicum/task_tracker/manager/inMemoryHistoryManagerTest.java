package ru.practicum.task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddNewTask() {
        Task task = new Task("1", "Test Task 1");
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "History should contain one task.");
        assertEquals(task, history.get(0), "The task in history should be the same as the added task.");
    }

    @Test
    public void testAddExistingTask() {
        Task task1 = new Task("1", "Test Task 1");
        Task task2 = new Task("1", "Test Task 1 Updated");

        historyManager.add(task1);
        historyManager.add(task2); // Updating the existing task

        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "History should still contain one task after updating.");
        assertEquals(task2, history.get(0), "The task in history should be the updated task.");
    }

    @Test
    public void testRemoveTask() {
        Task task = new Task("1", "Test Task 1");
        historyManager.add(task);

        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "History should be empty after removing the task.");
    }

    @Test
    public void testGetHistoryOrder() {
        Task task1 = new Task("1", "Task 1");
        Task task2 = new Task("2", "Task 2");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // Add task1 again to check if it updates correctly

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "History should contain two tasks.");
        assertEquals(task1, history.get(1), "The last task should be the most recently viewed.");
        assertEquals(task2, history.get(0), "The first task should remain as it was.");
    }

    // Проверка на пустое удаление
    @Test
    public void testRemoveNonExistentTask() {
        historyManager.remove(999); // Removing a non-existent task should not throw an exception.
        assertDoesNotThrow(() -> historyManager.remove(999), "Removing a non-existent task should not throw an exception.");
    }



}
