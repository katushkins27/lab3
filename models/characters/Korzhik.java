package models.characters;
import models.locations.Location;

public class Korzhik extends Shorty{
    public Korzhik(String name, Location firstLocation, int attentiveness, int sleepiness){
        super(name, firstLocation, attentiveness, sleepiness);
    }

    @Override
    public void baseAction(){
        speak("Пеку коржики)");
    }
}
