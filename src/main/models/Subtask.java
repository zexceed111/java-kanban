package main.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String title, String description, Status status, Duration duration, LocalDateTime startTime, int epicId) {
        super(id, title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, Duration duration, LocalDateTime startTime, int epicId) {
        super(title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toStringForFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return String.format("%d,SUBTASK,%s,%s,%s,%d,%s", id, title, status, description, duration, startTime.format(formatter));
    }

    @Override
    public String toString() {
        return "Subtask{" + "epicId=" + epicId + ", title='" + title + '\'' + ", description='" + description + '\'' + ", id=" + id + ", status=" + status + ", duration=" + duration + ", startTime=" + startTime + '}';
    }
}
