package main.models;

public class ManagerIntersectionsException extends RuntimeException {
    public ManagerIntersectionsException(String message) {
        super(message);
    }

    public ManagerIntersectionsException(String message, Throwable cause) {
        super(message, cause);
    }

}
