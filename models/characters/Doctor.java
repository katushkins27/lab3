package models.characters;

import models.enums.HealthState;
import models.events.Event;
import models.interfaces.MedicalProf;
import models.locations.Location;

public class Doctor extends Shorty implements MedicalProf {
    private final int expirience;

    public Doctor(String name, Location firstLocation, int expirience) {
        super(name, firstLocation, 0, 0);
        this.expirience = Math.max(0, Math.min(100, expirience));
    }

    @Override
    public void baseAction(){
        System.out.printf("%s осматривает пациентов\n", name);
    }

    @Override
    public int calculateDays(Shorty patient){
        int baseDays = 3;
        int experienceBonus = expirience/20;
        int all_days = baseDays - experienceBonus;
        memory.add(new Event("Рассчитал лечение " + all_days + " день/дня/дней для" + patient.getName(), location.getName()));
        return Math.max(1, all_days);
    }
    @Override
    public String diagnose(Shorty patient){
        System.out.printf("Доктор %s осматривает %s\n", name, patient.getName());
        memory.add(new Event("Продиагностировал" + patient.getName(), location.getName()));
        String res = "";
        if (patient.getHealthState() == HealthState.SICK){
            res = String.format("%s болен, необходимо лечение", patient.getName());
            int days = calculateDays(patient);
            String needtime = String.format("Лечение займет %d %s", days, "день/дня/дней");
            res = res + ". " + needtime;
        } else{
            res = String.format("диагноз: %s здоров\n", patient.getName());
        }
        return res;
    }
    @Override
    public void treat(Shorty patient){
        memory.add(new Event("Лечит: " + patient.getName(), location.getName()));
        if (patient.getHealthState() == HealthState.SICK){
            System.out.printf("%s лечит %s\n", name, patient.getName());
            patient.recover();
        }
    }
    @Override
    public String toString(){
        return String.format("Доктор {имя = %s, опыт = %d, местоположение = %s}", name, expirience, location.getName());
    }

}
