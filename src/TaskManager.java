import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public List<Task> getSubtasksOfEpic(int epicId) {
        return tasks.values().stream()
                .filter(task -> task instanceof Subtask && ((Subtask) task).getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    public void updateEpicStatus(int epicId) {
        List<Task> subtasks = getSubtasksOfEpic(epicId);
        Status newStatus = determineEpicStatus(subtasks);
        Epic epic = (Epic) tasks.get(epicId);
        epic.setStatus(newStatus);
    }

    private Status determineEpicStatus(List<Task> subtasks) {
        if (subtasks.isEmpty() || subtasks.stream().allMatch(t -> t.getStatus() == Status.NEW)) {
            return Status.NEW;
        } else if (subtasks.stream().allMatch(t -> t.getStatus() == Status.DONE)) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }
}
