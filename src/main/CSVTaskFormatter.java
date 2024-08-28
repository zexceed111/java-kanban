package main;

import main.models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {


    static String makeDataToSave(List<Task> tasks, List<Subtask> subtasks, List<Epic> epics, HistoryManager historyManager) {

        StringBuilder history = new StringBuilder();
        history.append("id,type,name,status,description,duration,startTime,epic or subtasks\n");

        for (Task task : tasks) {
            history.append(task.toStringForFile()).append("\n");
        }

        for (Subtask subtask : subtasks) {
            history.append(subtask.toStringForFile()).append("\n");
        }
        for (Epic epic : epics) {
            history.append(epic.toStringForFile()).append("\n");
        }
        history.append("\n");
        if (historyManager.getHistory().isEmpty())
            history.append("empty history");
        else {
            history.append(historyToString(historyManager));
        }
        return history.toString();
    }


    static String historyToString(HistoryManager manager) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (var task : manager.getHistory()) {
            if (i == 0) {
                result.append(task.getId());
            } else {
                result.append(",").append(task.getId());
            }
            i++;
        }
        return result.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> result = new ArrayList<>();
        if (value.equals("empty history"))
            return result;
        String[] historyIds = value.split(",");
        for (String id : historyIds) {
            result.add(Integer.parseInt(id));
        }
        return result;
    }


    public static Task fromString(String value) {

        String[] data = value.split(",");
        int id = Integer.parseInt(data[0]);
        String title = data[2];
        Status status = Status.valueOf(data[3]);
        String description = data[4];
        TaskType type = TaskType.valueOf(data[1]);
        int duration = Integer.parseInt(data[5]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(data[6], formatter);
        switch (type) {
            case TASK: {
                return new Task(id, title, description, status, Duration.ofMinutes(duration), startTime);
            }
            case SUBTASK: {
                int epicId = Integer.parseInt(data[5]);
                return new Subtask(id, title, description, status, Duration.ofMinutes(duration), startTime, epicId);
            }
            case EPIC: {
                return new Epic(id, title, description, status, Duration.ofMinutes(duration), startTime, null);
            }
            default: {
                return null;
            }
        }

    }
}