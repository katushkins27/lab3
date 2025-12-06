package models.characters;

import models.locations.Location;
import models.characters.Shorty;

public class Makovka extends Shorty{
    public Makovka(String name, Location firstLocation){
        super(name, firstLocation, 100, 0);
    }
    @Override
    public void baseAction(){
        speak("Гуляю в маковых полях");
    }
    @Override
    public String toString(){
        return String.format("Малышка{имя='%s', месоположение = %s}",
                name, location.getName());
    }
}
