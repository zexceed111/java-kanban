package main.models;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message, final Throwable cause) {
        super(message, cause);
    }
}