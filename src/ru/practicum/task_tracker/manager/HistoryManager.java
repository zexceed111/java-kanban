package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
class Node {
    Task task;
    Node next;
    Node prev;

    Node(Task task) {
        this.task = task;
    }
}
