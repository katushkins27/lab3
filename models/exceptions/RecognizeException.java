package models.exceptions;

public class RecognizeException extends Exception{
    public RecognizeException(String message){
        super(message);
    }
    @Override
    public String getMessage(){
        return "Ошибка при узнавании: " + super.getMessage();
    }
}