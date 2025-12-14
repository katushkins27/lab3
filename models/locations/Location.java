package models.locations;

import models.characters.Person;
import models.characters.Shorty;
import models.enums.HealthState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Location {
    /**
     * Класс Локаций определяет несколько методов,
     * которые просто возвращают определенные параметры.
     * И описывает работу двух методов, которые добавляют или удаляют персонажа из локации
     * На основании логики работы этих двух методов работает moveTo - метод для перемещения.
     * @see models.interfaces.Mover Интерфейс для перемещения между локациями
     */
    private final String name;
    private final List<Person> occpants;
    private final int lighting;
    private final boolean isPublic;
    private final int temperature;

    public Location(String name, int lighting, int temperature, boolean isPublic){
        this.name = name;
        this.lighting = lighting;
        this.isPublic = isPublic;
        this.occpants = new ArrayList<>();
        this.temperature = temperature;
    }
    public String getName(){
        return name;
    }
    public int getLighting(){
        return lighting;
    }
    public boolean isPublic(){
        return isPublic;
    }
    public void addOccupant(Person person){
        if (! occpants.contains(person)){
            occpants.add(person);
        }
    }
    public void removeOccupant(Person person){
        occpants.remove(person);
    }
    public void degreeOfIllness(Shorty shorty){
        if (temperature < -30){
            shorty.getSick(HealthState.SERIOUSLI_ILL);
        } else if (temperature < -20) {
            shorty.getSick(HealthState.SICK);
        } else if (temperature < -10) {
            shorty.getSick(HealthState.MILDLY_ILL);
        } else if (temperature < 0) {
            shorty.getSick(HealthState.NOT_FEELING_WELL);
        } else {
            shorty.getSick(HealthState.ALMOST_HEALTHY);
        }
    }

    /**
     * Сравниваем локации: они равны, если у них одинаковые имена.
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof Location)) return false;
        Location location = (Location) obj;
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name);
    }

    /**
     * Возвращает описание локации в строковом формате.
     */
    @Override
    public String toString(){
        return String.format("Локация{название=%s, освещенность=%s, публичность=%s, количсество людей=%s}",
                name, lighting, isPublic, occpants.size());
    }
}
