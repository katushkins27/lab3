package models.interfaces;
import models.characters.Shorty;

public interface Diagnoser {
    /**
     * Ставит диагноз пациенту
     * @param patient пациент для диагностики
     * @return строку с диагнозом, или "Не нуждается в лечении" если здоров
     */
    String diagnose(Shorty patient);
    /**
     * Рассчитывает дни лечения
     * @return количество дней, или 0 если здоров
     */
    int calculateDays(Shorty patient);
}
