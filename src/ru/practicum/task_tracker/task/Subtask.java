package ru.practicum.task_tracker.task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int epicId, String name, String description, Status status) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int epicId, Integer id, String name, String description, Status status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" + "id=" + this.getId() + ", name='" + this.getName() + '\'' + ", description='" + this.getDescription() + '\'' + ", status=" + this.getStatus() + ", epicId=" + epicId + '}' + "\n";
    }
}
