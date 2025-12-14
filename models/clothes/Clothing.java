package models.clothes;

import models.characters.Person;
import models.enums.EmotionalState;
import models.enums.Size;
import models.enums.TypeClothes;
import java.util.Objects;

public class Clothing {
    private Person person;
    private final TypeClothes type;
    private final Size size;
    private final String color;

    public Clothing(TypeClothes type, Size size, String color, Person firstPerson){
        this.type = type;
        this.size = size;
        this.color = color;
        this.person = firstPerson;
    }
    /**
     * Метод меняет владельца одежды.
     */
    public void changePerson(Person newPerson){
        this.person = newPerson;
    }
    /**
     * Метод возвращает полное описание одежды в понятном виде для пользователя.
     */
    public String getDescription(){
        return String.format("%s %s %s", color, type.getDescription(), size.getDescription());
    }

    /**
     * Сравнивает объекты одежды по типу, размеру и цвету.
     * Владелец не учитывается при сравнении.
     * @see Objects#equals(Object, Object) для безопасного сравнения строк
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof Clothing)) return false;
        Clothing clothing = (Clothing) obj;
        return type == clothing.type && size == clothing.size && Objects.equals(color, clothing.color);
    }

    @Override
    public int hashCode(){
        return Objects.hash(type, color, size);
    }

    /**
     * Возвращает строковое описание одежды.
     * Если владелец не указан, отображается "null".
     */
    @Override
    public String toString(){
        String personName;
        if (person != null){
            personName = person.getName();
        } else {
            personName = "null";
        }
        return String.format("Одежда {тип = %s, цвет = %s, размер = %s, владелец = %s}", type, color, size, personName);
    }
}
