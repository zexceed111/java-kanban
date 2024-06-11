public class Subtask extends Task {
    Status status;

    public Subtask(String nameTask, String descriptionTask, int index) {
        super(nameTask, descriptionTask, index);
        status = Status.NEW;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
