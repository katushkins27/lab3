package models.characters;
import models.enums.BaseAct;
import models.locations.Location;
import models.enums.*;

public class Svistulkin extends Shorty {
    public Svistulkin(String name, Location firstlocation, int attentiveness, int sleepiness){
        super(name, firstlocation, attentiveness, sleepiness);
    }
    @Override
    public void baseAction(){
        BaseAct baseAct = BaseAct.getRandomAct();
        speak(baseAct.getDescription() + " будучи " + emotionalState.getDescription());
    }
}
