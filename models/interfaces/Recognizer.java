package models.interfaces;
import models.characters.Person;
public interface Recognizer {
    boolean recognizeChance(Person other, String context);
}
