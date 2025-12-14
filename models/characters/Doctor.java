package models.characters;

import models.enums.HealthState;
import models.events.Event;
import models.interfaces.*;
import models.locations.Location;

import java.util.*;

public class Doctor extends Shorty implements Diagnoser, Treater {
    private final int expirience;
    private final Map<String, List<String>> medicalHistory = new HashMap<>();

    public Doctor(String name, Location firstLocation, int expirience) {
        super(name, firstLocation, 0, 0);
        this.expirience = Math.max(0, Math.min(100, expirience));
    }

    @Override
    public void baseAction(){
        System.out.printf("%s осматривает пациентов\n", name);
    }

    /**
     * Метод высчитывает дни, необходимые для лечения.
     * Находится индекс нашего состояния, высчитываем сколько ступеней осталось до выздоравления,
     * Применяем опыт врача и прибавляем дни для лечения в зависимости от тяжести заболевания.
     * @param patient
     */
    @Override
    public int calculateDays(Shorty patient){
        HealthState currentState = patient.getHealthState();
        if (currentState == HealthState.HEALTHY) {
            return 0;
        }
        HealthState[] states = HealthState.values();
        int currentIndex = -1;
        for (int i = 0; i < states.length; i++) {
            if (states[i] == currentState) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex == -1) return 3;
        int baseDays = (states.length - currentIndex - 1) * 2;
        int experienceBonus = expirience / 25;
        int dopDays = patient.getHealthState().getExtraDays();
        int all_days = baseDays - experienceBonus + dopDays;
        memory.add(new Event("Рассчитал лечение " + all_days + " день/дня/дней для" + patient.getName(), location.getName()));
        return Math.max(1, all_days);
    }

    /**
     * Врач осматривает пациента, заносит запись о диагнозе в его медкнижку
     * @see #printMedHistory(Shorty) Метод для вывода данных из медкнижки
     * @param patient
     */
    @Override
    public String diagnose(Shorty patient){
        HealthState healthState = patient.getHealthState();
        System.out.printf("%s осматривает %s\n", name, patient.getName());
        memory.add(new Event("Продиагностировал" + patient.getName(), location.getName()));
        String res = "";
        if (healthState != HealthState.HEALTHY){
            res = String.format("%s %s, необходимо лечение", patient.getName(),
                    healthState.getDescription());

            int days = calculateDays(patient);

            String needTime = String.format("Чтобы вылечить малыша, понадобится примерно %d %s",
                    days, "день/дня/дней");

            List<String> history = medicalHistory.getOrDefault(patient.getName(), new ArrayList<>());
            history.add(needTime);
            medicalHistory.put(patient.getName(), history);
            res = res + ". " + needTime;
        } else{
            res = String.format("диагноз: %s здоров\n", patient.getName());
        }
        return res;
    }

    /**
     * Аналогично, как и до этого рассчитываем необходимое количество ступеней для выздоровления
     * Каждый день выводим сколько дней прошло, при наличии улучшений - выводим их.
     * Также сдвигаемя на ступень ближе к выздоравлению.
     * @see #distributeDays(int, int) Распределяем сколько дней уйдет на каждый период восстановления
     * @param patient
     */
    @Override
    public void treat(Shorty patient) {
        HealthState currentState = patient.getHealthState();
        if (currentState == HealthState.HEALTHY) {
            System.out.println(patient.getName() + " уже здоров");
            return;
        }
        int plannedDays = calculateDays(patient);

        HealthState[] allStates = HealthState.values();
        int currentIndex = -1;
        for (int i = 0; i < allStates.length; i++) {
            if (allStates[i] == currentState) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) return;
        int stepsToHealth = allStates.length - currentIndex - 1;

        System.out.printf("\nЛечение %s\n", patient.getName());
        System.out.printf("Начальное состояние: %s\n", currentState.getDescription());
        System.out.printf("План: %d дней\n\n", plannedDays);

        int[] daysPerStep = distributeDays(plannedDays, stepsToHealth);
        int totalDaysTreated = 0;
        for (int i = 0; i < stepsToHealth; i++) {
            int daysForThisStep = daysPerStep[i];
            for (int day = 1; day <= daysForThisStep; day++) {
                totalDaysTreated++;
                memory.add(new Event(String.format("День %d/%d: лечение %s",
                        totalDaysTreated, plannedDays, patient.getName()), location.getName()));

                System.out.printf("Пройдено дней: %d/%d\n", totalDaysTreated, plannedDays);
            }
            currentIndex++;
            HealthState newState = allStates[currentIndex];
            patient.healthState = newState;

            System.out.printf("Улучшение: %s %s\n",
                    patient.getName(), newState.getDescription());

            memory.add(new Event(String.format("%s улучшение состояния: %s",
                    patient.getName(), newState.getDescription()), location.getName()));
        }

        System.out.printf("\n%s полностью здоров!\n", patient.getName());
        System.out.printf("Фактическое время: %d дней (план: %d дней)\n",
                totalDaysTreated, plannedDays);

        memory.add(new Event(String.format("%s вылечил %s за %d дней", name, patient.getName(),
                totalDaysTreated), location.getName()));
    }

    /**
     * Расчитываем сколько дней нужно на каждую ступень выздоравления.
     * Если количество не крастно ступеням лечения, то доп дни прибавляются в более ранние ступени лечения.
     * @param totalDays
     * @param steps
     * @return
     */
    private int[] distributeDays(int totalDays, int steps) {
        int[] distribution = new int[steps];

        int baseDays = totalDays / steps;
        int extraDays = totalDays % steps;

        for (int i = 0; i < steps; i++) {
            distribution[i] = baseDays;
            if (i < extraDays) {
                distribution[i]++;
            }
        }

        return distribution;
    }

    public void printMedHistory(Shorty patient){
        if (!medicalHistory.containsKey(patient.getName())) {
            System.out.println("Записей нет");
        }
        List<String> history = medicalHistory.get(patient.getName());
        System.out.printf("\nИстория болезни %s\n", patient.getName());
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("%d. %s\n\n", i + 1, history.get(i));
        }
    }
    @Override
    public String toString(){
        return String.format("Доктор {имя = %s, опыт = %d, местоположение = %s}", name, expirience, location.getName());
    }

}
