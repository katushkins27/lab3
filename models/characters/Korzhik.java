package models.characters;
import models.enums.BaseAct;
import models.locations.Location;

public class Korzhik extends Shorty{
    public Korzhik(String name, Location firstLocation, int attentiveness, int sleepiness){
        super(name, firstLocation, attentiveness, sleepiness);
    }

    @Override
    public void baseAction(){
        BaseAct baseAct = BaseAct.getRandomAct();
        speak(baseAct.getDescription() + " будучи " + emotionalState.getDescription());
    }
}
