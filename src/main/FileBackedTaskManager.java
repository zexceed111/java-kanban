package main;


import main.models.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static main.CSVTaskFormatter.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File autoSaveFileName;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        autoSaveFileName = file;
    }


    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        try {

            String data = Files.readString(file.toPath());

            String[] lines = data.split("\n");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
            int size = lines.length;

            for (int i = 1; i < size - 2; i++) {
                var task = fromString(lines[i]);
                if (task instanceof Epic) {
                    fileBackedTaskManager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.addSubtask((Subtask) task);
                } else {
                    fileBackedTaskManager.addTask(task);
                }
            }

            List<Integer> history = historyFromString(lines[size - 1]);
            if (!history.isEmpty()) {
                for (int idTask : history) {
                    if (fileBackedTaskManager.getTaskHashMap().containsKey(idTask))
                        fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getTask(idTask));
                    if (fileBackedTaskManager.getSubtaskHashMap().containsKey(idTask))
                        fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getSubtask(idTask));
                    if (fileBackedTaskManager.getEpicHashMap().containsKey(idTask))
                        fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getEpic(idTask));
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка работы с файлом", e);
        }
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(autoSaveFileName, StandardCharsets.UTF_8))) {
            wr.write(makeDataToSave(getTasks(), getSubtasks(), getEpics(), this.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка работы с файлом", e);
        }
    }

    @Override
    public Task getTask(int id) throws ManagerSaveException {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) throws ManagerSaveException {
        Subtask task = super.getSubtask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) throws ManagerSaveException {
        Epic task = super.getEpic(id);
        save();
        return task;
    }

    @Override
    public void deleteTasks() throws ManagerSaveException {
        super.deleteTasks();
        save();
    }

    @Override
    public void addTask(Task newTask) throws ManagerSaveException {
        super.addTask(newTask);
        save();
    }

    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void deleteTask(int id) throws ManagerSaveException {
        super.deleteTask(id);
        save();
    }

    @Override
    public void changeTaskStatus(Task task, Status status) throws ManagerSaveException {
        super.changeTaskStatus(task, status);
        save();
    }

    @Override
    public void deleteSubtasks() throws ManagerSaveException {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void addSubtask(Subtask newSubtask) throws ManagerSaveException {
        super.addSubtask(newSubtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) throws ManagerSaveException {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) throws ManagerSaveException {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void changeSubtaskStatus(Subtask subtask, Status status) throws ManagerSaveException {
        super.changeSubtaskStatus(subtask, status);
        save();
    }

    @Override
    public void deleteEpics() throws ManagerSaveException {
        super.deleteEpics();
        save();
    }

    @Override
    public void addEpic(Epic newEpic) throws ManagerSaveException {
        super.addEpic(newEpic);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) throws ManagerSaveException {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void removeEpicSubtask(Epic epic, int id) throws ManagerSaveException {
        super.removeEpicSubtask(epic, id);
        save();
    }

    @Override
    public void deleteEpic(int id) throws ManagerSaveException {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) throws ManagerSaveException {
        super.updateEpicStatus(epic);
        save();
    }

}