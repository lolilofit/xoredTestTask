package xored.testtask.usova.cell;

public class CellException extends Exception {
    private String message;

    public CellException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
