package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> memory = new ArrayList<>(10);

    @Override
    public void add(Task task) {
        if (memory.size() == 10) {
            memory.removeFirst();
        }
            memory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return memory;
    }

}

