package models.characters;
import models.locations.Location;

public class Svistulkin extends Shorty {
    public Svistulkin(String name, Location firstlocation, int attentiveness, int sleepiness){
        super(name, firstlocation, attentiveness, sleepiness);
    }
    @Override
    public void baseAction(){
        speak("Постоянно насвистываю веселую мелодию");
    }
}
