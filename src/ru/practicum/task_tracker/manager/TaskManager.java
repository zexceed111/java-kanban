package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Epic;
import ru.practicum.task_tracker.task.Subtask;
import ru.practicum.task_tracker.task.Task;

import java.util.List;

public interface TaskManager {

    public List<Task> getHistory();

    List<Task> getTasks();

    Task getByTaskId(Integer taskId);

    Task createTask(Task task);

    void updateTask(Task task);

    boolean deleteTask(int taskId);

    List<Epic> getEpics();

    Epic getByEpicId(Integer epicId);

    Epic createEpic(Epic epic);

    Epic updateEpic(Epic epic);

    boolean deleteEpic(int epicId);

    void updateStatusEpic(Epic epic);

    List<Subtask> getSubtasks();

    Subtask getBySubtaskId(Integer subtaskId);

    Subtask createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    boolean deleteSubtask(Integer id);

    void deleteAllTasks();
}
