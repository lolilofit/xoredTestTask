package xored.testtask.usova.operations;

public class OperationException extends Exception {
    private String message;

    public OperationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
