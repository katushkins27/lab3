package models.characters;

import models.events.Event;
import models.exceptions.MovementException;
import models.exceptions.RecognizeException;
import models.interfaces.*;
import models.locations.Location;
import models.enums.EmotionalState;

import java.util.ArrayList;
import java.util.*;
/**
 * Абстрактный базовый класс для всех персонажей в системе.
 * Предоставляет общую функциональность: перемещение, речь, рассказ историй,
 * распознавание других персонажей и занесение в память событий.
 * @see Speaker Интерфейс для возможности говорить
 * @see StoryTeller Интерфейс для рассказа историй
 * @see Mover Интерфейс для перемещения между локациями
 * @see Recognizer Интерфейс для распознавания других персонажей
 * @see EmotionalState Перечисление возможных эмоциональных состояний
 */
public abstract class Person implements Speaker, StoryTeller, Mover, Recognizer {
    protected final String name;
    protected Location location;
    protected int attentiveness;
    protected EmotionalState emotionalState;
    protected final List<Event> memory;

    /**
     * Конструктор для создания нового персонажа.
     * Пусть начальное состояние - спокойное.
     */
    public Person(String name, Location firstlocation, int attentiveness){
        this.name = name;
        this.location = firstlocation;
        // Гарантируем, что внимательность находится в диапазоне 0-100
        this.attentiveness = Math.max(0, Math.min(100, attentiveness));
        this.memory = new ArrayList<>();
        this.emotionalState = EmotionalState.CALM;
        firstlocation.addOccupant(this);
    }

    /**
     * Сразу обозначаем несколько методов,
     * возвращающих значения имени, локации, внимательности.
     */
    public String getName(){
        return name;
    }
    public Location getLocation(){
        return location;
    }
    public int getAttentiveness() {
        return attentiveness;
    }

    /**
     * По заданию в абстрактном классе должен существовать
     * как минимум один абстрактный метод, он переопределяется
     * в классах конкретных персонажей.
     */
    public abstract void baseAction();

    /**
     * Устанавливаем новое эмоциональное состояние персонажа.
     * Автоматически записываем событие в память и выводит сообщение об изменении.
     * @see EmotionalState
     */
    public void setEmotionalState(EmotionalState state){
        this.emotionalState = state;
        memory.add(new Event("Изменилась эмоция на " + state, location.getName()));
        System.out.printf("%s %s\n", name, emotionalState.getDescription());
    }

    /**
     * Перемещаем персонажа в указанную локацию.
     * Обновляем списки персонажей в старой и новой локациях.
     * @param newLocation целевая локация для перемещения
     * @throws MovementException если целевая локация равна null
     * @throws MovementException если персонаж не может покинуть текущую локацию
     * @see Mover#moveTo(Location)
     */
    @Override
    public void moveTo(Location newLocation) throws MovementException{
        if (newLocation == null){
            throw new MovementException("Не могу переместиться в место, которого не существует");
        }
        System.out.printf("%s перемещается из %s в %s\n", name, location.getName(), newLocation.getName());

        location.removeOccupant(this);
        this.location = newLocation;
        newLocation.addOccupant(this);

        memory.add(new Event("Перемистился в" + newLocation.getName(), location.getName()));
    }

    /**
     * Произносим сообщение от имени персонажа.
     * Выводим сообщение в консоль и записывает его в память персонажа.
     * @see Speaker#speak(String)
     */
    @Override
    public void speak(String message){
        System.out.printf("%s говорит: %s\n", name, message);
        memory.add(new Event("Сказал: " + message, location.getName()));
    }

    /**
     * Рассказваем историю от имени персонажа.
     * Выводим сообщение в консоль и записывает его в память персонажа.
     * @see Speaker#speak(String)
     */
    @Override
    public void tellStory(String story, Person listener){
        System.out.printf("%s рассказывает %s %s\n", name, listener.getName(), story);
        memory.add(new Event("Расказал историю: " + story, location.getName()));
    }

    /**
     * Пытаемся распознать другого персонажа с учётом его местоположения.
     * Вероятность успеха зависит от внимательности текущего персонажа и контекста.
     * Базовая вероятность равна уровню внимательности.
     * Если внимательность меньше 50%, шанс распознавания равен 0%,
     * так как если внимательность меньше 50%, то герои не увидят вообще Свистулькина по сценарию.
     * В больнице шанс увеличивается на 20%, в тёмном доме уменьшается на 20%.
     * @see Recognizer#recognizeChance(Person, String)
     */
    @Override
    public boolean recognizeChance(Person other, String context) throws RecognizeException{
        if (other == null){
            throw new RecognizeException("Не могу узнать, если узнавать некого");
        }
        int chanceofRecognize = attentiveness;

        if (chanceofRecognize < 50){
            chanceofRecognize = 0;
        } else if ("hospital".equals(context)){
            chanceofRecognize += 20;
        } else if ("dark_home".equals(context)){
            chanceofRecognize -=20;
        }
        boolean recognized = new Random().nextInt(100) < chanceofRecognize;

        if (recognized) {
            System.out.printf("%s узнает %s\n", name, other.getName());
            String result = "узнал";
            memory.add(new Event(result + ' ' + other.getName(), location.getName()));
        } else {
            System.out.printf("%s не узнает %s\n", name, other.getName());
            String result = "не узнал";
            memory.add(new Event(result + ' ' + other.getName(), location.getName()));
        }
        return recognized;
    }

    /**
     * Выводит всю память персонажа в консоль,
     * то есть мы можем в любой момент программы посмотреть состояние персонажа.
     * Используется для демонстрации накопленных воспоминаний.
     * Если память пуста, выводится соответствующее сообщение.
     */
    public void printMemory(){
        System.out.println("Память " + name);
        if (memory.isEmpty()){
            System.out.println("ничего нет в памяти");
        } else {
            for (Event event: memory){
                System.out.println(event);
            }
        }
        System.out.println("Состояние сейчас: " + this);
    }

    /**
     * Сравниваем персонажей по имени.
     * Два персонажа считаются равными, если у них одинаковые имена.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Возвращаем строковое представление персонажа.
     * Включает имя, уровень внимательности,
     * эмоциональное состояние и текущую локацию.
     */
    @Override
    public String toString() {
        return String.format("Персонаж{имя='%s', внимательность=%d, эмоции=%s, местоположение=%s}",
                name, attentiveness, emotionalState.getDescription(), location.getName());
    }
}
