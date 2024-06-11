import java.util.ArrayList;
import java.util.HashMap;


public class Task {

        String nameTask;
        String descriptionTask;
        int index;

        public Task(String nameTask, String descriptionTask, int index){
            this.nameTask = nameTask;
            this.descriptionTask = descriptionTask;
            this.index = index;
        }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Task{" +
                "code= " + index +
                ", name='" + nameTask + '\'' +
                ", description='" + descriptionTask + '\'' +
                '}';
    }
}
