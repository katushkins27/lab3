package models.interfaces;
import models.characters.Shorty;

public interface Treater {
    /**
     * Лечит пациента
     * @param patient пациент для лечения
     */
    void treat(Shorty patient);
}
