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

            assertEquals(1, history.size(), "История должна содержать одну задачу.");
            assertEquals(task, history.get(0), "Задача в истории должна быть такой же, как и добавленная задача.");
        }

        @Test
        public void testAddExistingTask() {
            Task task1 = new Task("1", "Test Task 1");
            Task task2 = new Task("1", "Test Task 1 Updated");

            historyManager.add(task1);
            historyManager.add(task2); // Updating the existing task

            List<Task> history = historyManager.getHistory();

            assertEquals(1, history.size(), "После обновления история все еще должна содержать одну задачу.");
            assertEquals(task2, history.get(0), "Задачей в истории должна быть обновленная задача.");
        }



        // Проверка на пустое удаление
        @Test
        public void testRemoveNonExistentTask() {
            historyManager.remove(999); // Removing a non-existent task should not throw an exception.
            assertDoesNotThrow(() -> historyManager.remove(999), "Удаление несуществующей задачи не должно вызывать исключение.");
        }

        @Test
        public void testGetHistoryEmpty() {
            List<Task> history = historyManager.getHistory();
            assertTrue(history.isEmpty(), "История должна быть пустой.");
        }




    }
