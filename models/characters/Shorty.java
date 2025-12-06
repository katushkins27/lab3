package models.characters;

import models.interfaces.*;
import models.locations.Location;
import models.clothes.Clothing;
import models.enums.HealthState;
import models.events.Event;
import java.util.Random;

/**
 * Класс, описывающий коротышку - базового персонажа с возможностью
 * носить одежду и засыпать в соответствии с дополнительными параметрами при создании.
 * @see Person Базовый класс персонажа
 * @see ClothingWear Интерфейс для работы с одеждой
 * @see Sleeper Интерфейс для поведения сна
 * @see HealthState Перечисление состояний здоровья
 */
public class Shorty extends Person implements ClothingWear, Sleeper {
    protected boolean isSleeping;
    protected HealthState healthState;
    protected Clothing outfit;
    protected final int sleepiness;

    public Shorty(String name, Location firstlocation, int attentiveness, int sleepiness) {
        super(name, firstlocation, attentiveness);
        this.isSleeping = false;
        this.healthState = HealthState.HEALTHY;
        this.sleepiness = Math.max(0, Math.min(100, sleepiness));
    }

    /**
     * Возвращаем текущее состояние здоровья персонажа.
     * @see HealthState
     * Возвращаем одежду, которую носит персонаж в данный момент.
     * @see ClothingWear#getClothes()
     * Устанавливаем новую одежду для персонажа.
     * @see ClothingWear#setClothes(Clothing)
     */
    public HealthState getHealthState() {
        return healthState;
    }

    @Override
    public Clothing getClothes() {
        return outfit;
    }

    @Override
    public void setClothes(Clothing clothes) {
        this.outfit = outfit;
    }

    /**
     * Надеваем новую одежду на персонажа и обновляем владельца одежды.
     * @see ClothingWear#wearClothes(Clothing)
     * @see Clothing#changePerson(Person)
     */
    @Override
    public void wearClothes(Clothing newClothes) {
        this.outfit = newClothes;
        if (newClothes != null) {
            newClothes.changePerson(this);
        }
    }

    /**
     * Заставляем персонажа заснуть в текущей локации.
     * @see Sleeper#fallAsleep()
     */
    @Override
    public void fallAsleep() {
        isSleeping = true;
        System.out.printf("%s засыпает в %s\n", name, location.getName());
        memory.add(new Event("Засыпает", location.getName()));
    }

    /**
     * Будим персонажа.
     * @see Sleeper#wakeUp()
     */
    @Override
    public void wakeUp() {
        isSleeping = false;
        System.out.printf("%s просыпается в %s и собирается уходить\n", name, location.getName());
        memory.add(new Event("Просыпается", location.getName()));
    }

    /**
     * Персонаж пытается заснуть в указанной локации с учётом условий.
     * Вероятность успеха зависит от сонливости, освещенности и публичности локации.
     * @see Sleeper#maybeSleep(Location)
     * @see Location#getLighting() Проверка освещённости локации
     * @see Location#isPublic() Проверка публичности локации
     */
    @Override
    public boolean maybeSleep(Location location) {
        int sleepChance = sleepiness;

        if (location.getLighting() < 30) {
            sleepChance += 20;
        }
        if (location.isPublic()) {
            sleepChance -= 30;
        }
        boolean fallsleep = new Random().nextInt(100) < sleepChance;
        String sleepResult;
        if (fallsleep) {
            sleepResult = "успешно";
        } else {
            sleepResult = "не удалось";
        }
        memory.add(new Event("Попытка заснуть: " + sleepResult, location.getName()));
        return fallsleep;
    }

    // Реализация абстрактного метода, переопределяется в классах наследниках - классы конкретных персонажей
    public void baseAction() {
        speak("Я выполняю своё обычное дело");
    }

    /**
     * Персонаж заболевает, меняется его состояние здоровья.
     * Фиксируем событие в памяти.
     */
    public void getSick() {
        healthState = HealthState.SICK;
        System.out.printf("%s простудился в %s и заболел\n", name, location.getName());
        memory.add(new Event("Заболел", location.getName()));
    }

    /**
     * Выздоровление персонажа, меняем его состояние здоровья.
     * Фиксируем событие выздоровления в памяти.
     */
    public void recover() {
        healthState = HealthState.HEALTHY;
        System.out.printf("%s выздоровел\n", name);
        memory.add(new Event("Выздоровел", location.getName()));
    }

    /**
     * Персонаж пытается надеть одежду и может по ошибке надеть чужую.
     * Вероятность ошибки зависит от внимательности персонажа.
     * @see Clothing#getDescription() Получение описания одежды для вывода
     */
    public boolean maybeWearWrong(Clothing wrongClothes, Clothing correctClothes) {
        int mistakeChance = 100 - attentiveness;

        boolean madeMistake = new Random().nextInt(100) < mistakeChance;
        if (madeMistake && wrongClothes != null) {
            System.out.printf("%s по ошибке надевает чужую/ой %s размера, вместо своей/го %s размера!\n",
                    name, wrongClothes.getDescription(), correctClothes.getDescription());
            wearClothes(wrongClothes);
            return true;
        }
        return false;

    }
}