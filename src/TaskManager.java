import java.util.HashMap;

public class TaskManager {

    int taskCounter = 0;
    int epicCounter = 0;

    HashMap<Integer, Task> taskTable = new HashMap<>();
    HashMap<Integer, Subtask> subtaskTable = new HashMap<>();
    HashMap<Integer, Epic> epicTable= new HashMap<>();

    public Task getTaskByNumber(Integer number){
        return taskTable.get(number);
    }

    public void makeTask(String nameTaks, String descriptionTask){
        Task task = new Task(nameTaks, descriptionTask, ++taskCounter);
        taskTable.put(task.index, task);
    }

    public void makeSubtask(String nameSubtask, String descriptionSubtask, int index){
        Subtask subtask = new Subtask(nameSubtask, descriptionSubtask, index);
        subtaskTable.put(index, subtask);
    }


}
