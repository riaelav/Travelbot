package travelbot.demo.exceptions;


import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        super("Validation failed with multiple errors.");
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
