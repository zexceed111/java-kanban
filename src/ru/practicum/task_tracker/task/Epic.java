package ru.practicum.task_tracker.task;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Subtask subtask) {
        if (subtask.getId() == this.getId()) {
            return;
        }
        subtasks.add(subtask);
    }

    public void clearsubtaskIds() {
        subtasks.clear();
    }

    public void removesubtaskById(Subtask subtask) {
        subtasks.remove(subtask);
    }

    @Override
    public String toString() {
        return "Epic{" + "id=" + this.getId() + ", name='" + this.getName() + '\'' + ", description='" + this.getDescription() + '\'' + ", status=" + this.getStatus() + ", subtasks=" + subtasks + '}' + "\n";
    }

}
