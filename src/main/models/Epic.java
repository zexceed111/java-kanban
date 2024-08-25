package main.models;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksIds;

    public Epic(int id, String title, String description, Status status, ArrayList<Integer> subtasksIds) {
        super(id, title, description, status);
        this.subtasksIds = subtasksIds;
    }

    public Epic(String title, String description, Status status, ArrayList<Integer> subtasksIds) {
        super(title, description, status);
        this.subtasksIds = subtasksIds;
    }

    private String getSubtasks() {
        String result = "";
        if (subtasksIds != null) {
            for (int i : subtasksIds) {
                result = result + i + " ";
            }
        }
        return result;
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }


    public void removeSubtask(int id) {
        if (!this.subtasksIds.contains(id))
            return;
        int index = this.subtasksIds.indexOf(id);
        this.subtasksIds.remove(index);

    }

    public void addSubtaskId(int id) {
        subtasksIds.add(id);
    }

    public void clearAllSubtasks() {

        this.subtasksIds.clear();
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        return "main.models.Epic{" +
                "subtasksIds=" + subtasksIds +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public String toStringForFile() {
        return String.format("%d,EPIC,%s,%s,%s,%s", id, title, status, description, getSubtasks());
    }
}
