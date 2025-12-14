package models.characters;

import models.enums.BaseAct;
import models.locations.Location;
import models.characters.Shorty;

public class Makovka extends Shorty{
    public Makovka(String name, Location firstLocation){
        super(name, firstLocation, 100, 0);
    }
    @Override
    public void baseAction(){
        BaseAct baseAct = BaseAct.getRandomAct();
        speak(baseAct.getDescription() + " будучи " + emotionalState.getDescription());
    }

    @Override
    public String toString(){
        return String.format("Малыш {имя='%s', местоположение = %s}",
                name, location.getName());
    }
}
