package models.interfaces;
import models.characters.Person;
import models.exceptions.RecognizeException;

public interface Recognizer {
    boolean recognizeChance(Person other, String context) throws RecognizeException;
}
