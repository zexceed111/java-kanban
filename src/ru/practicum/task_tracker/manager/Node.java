package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

class Node {
    Task task;
    Node next;
    Node prev;

    Node(Task task) {
        this.task = task;
    }
}
