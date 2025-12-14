package models.exceptions;

public class ClothingException extends Exception {
    public ClothingException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Ошибка с одеждой: " + super.getMessage();
    }
}
