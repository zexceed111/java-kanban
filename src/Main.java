import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание эпика
        Epic epic1 = new Epic("Epic 1", "Epic Description 1", 1);
        manager.createTask(epic1);

        // Создание подзадач
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", 2, 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", 3, 1);
        manager.createTask(subtask1);
        manager.createTask(subtask2);

        // Создание обычной задачи
        Task task1 = new Task("Task 1", "Task Description 1", 4);
        manager.createTask(task1);

        // Вывод всех задач
        System.out.println("All tasks:");
        manager.getAllTasks().forEach(System.out::println);

        // Обновление статуса подзадачи и эпика
        subtask1.setStatus(Status.DONE);
        manager.updateTask(subtask1);
        subtask2.setStatus(Status.DONE);
        manager.updateTask(subtask2);

        // Автоматическое обновление статуса эпика
        manager.updateEpicStatus(1);

        // Вывод обновленных задач
        System.out.println("\nUpdated tasks:");
        manager.getAllTasks().forEach(System.out::println);
    }
}
