package main;

import main.models.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TaskManager {
    Map<Integer, Task> getTaskHashMap();

    Map<Integer, Epic> getEpicHashMap();

    Map<Integer, Subtask> getSubtaskHashMap();

    List<Task> getHistory();


    Collection<Task> getPrioritizedTasks();

    int getNewId();

    int getId();

    List<Task> getTasks();


    void deleteTasks() throws ManagerSaveException;

    Task getTask(int id) throws ManagerSaveException;


    void addTask(Task newTask) throws ManagerSaveException;

    void updateTask(Task newTask) throws ManagerSaveException;

    void deleteTask(int id) throws ManagerSaveException;

    void changeTaskStatus(Task task, Status status) throws ManagerSaveException;

    List<Subtask> getSubtasks();

    void deleteSubtasks() throws ManagerSaveException;

    Subtask getSubtask(int id) throws ManagerSaveException;

    void addSubtask(Subtask newSubtask) throws ManagerSaveException;


    void updateSubtask(Subtask newSubtask) throws ManagerSaveException;

    void deleteSubtask(int id) throws ManagerSaveException;

    void changeSubtaskStatus(Subtask subtask, Status status) throws ManagerSaveException;

    boolean hasAllSubtaskSameStatus(Status status, Epic epic);

    List<Epic> getEpics();

    void deleteEpics() throws ManagerSaveException;

    Epic getEpic(int id) throws ManagerSaveException;

    void addEpic(Epic newEpic) throws ManagerSaveException;

    void updateEpic(Epic newEpic) throws ManagerSaveException;

    void removeEpicSubtask(Epic epic, int id) throws ManagerSaveException;

    void deleteEpic(int id) throws ManagerSaveException;

    void updateEpicStatus(Epic epic) throws ManagerSaveException;


}