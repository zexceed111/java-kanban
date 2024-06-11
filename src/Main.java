public class Main {

    public static void main(String[] args) {

        String name1 = "Переезд";
        String name2 = "Собрать вещи";
        TaskManager taskManager = new TaskManager();
        taskManager.makeTask(name1, name2);

        name1 = "еда";
        name2 = "Бутерброд";
        taskManager.makeTask(name1, name2);
        System.out.println(taskManager.taskTable);

        Task task = taskManager.getTaskByNumber(1);
        taskManager.makeSubtask(task.nameTask, task.descriptionTask, task.index);
        System.out.println(taskManager.subtaskTable);
    }
}
