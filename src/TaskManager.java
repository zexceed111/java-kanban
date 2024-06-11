import java.util.HashMap;

public class TaskManager {

    int taskCounter = 0;
    int epicCounter = 0;

    HashMap<Integer, Task> taskTable = new HashMap<>();

    public void makeTask(String nameTaks, String descriptionTask){
        Task task = new Task(nameTaks, descriptionTask, ++taskCounter);
        taskTable.put(taskCounter, task);
    }



}
