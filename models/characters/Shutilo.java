package models.characters;
import models.locations.Location;

public class Shutilo extends Shorty {
    public Shutilo(String name, Location firstlocation, int attentiveness, int sleepiness){
        super(name, firstlocation, attentiveness, sleepiness);
    }
    @Override
    public void baseAction(){
        speak("Рассказываю шутки");
    }
}
