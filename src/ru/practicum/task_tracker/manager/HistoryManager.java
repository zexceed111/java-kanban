package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.List;

public interface HistoryManager  {

    public  void add(Task task);
    public  List<Task> getHistory();

}
