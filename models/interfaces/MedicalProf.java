package models.interfaces;
import models.characters.Shorty;

public interface MedicalProf {
    void treat(Shorty patient);
    String diagnose(Shorty patient);
    int calculateDays(Shorty patient);
}
