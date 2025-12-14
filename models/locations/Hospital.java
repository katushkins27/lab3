package models.locations;
import models.characters.Shorty;
import models.characters.Doctor;
import models.exceptions.MovementException;
import java.util.*;

public class Hospital extends Location {
    /**
     * Класс Больница наследуется от класса Локации, имеет свои списки пациентов и врачей.
     * Описывает работу больницы путем создания регистрации пациента
     * @see #admitPatient(Shorty)
     * и также добавление лечащего врача
     * @see #addDoctor(Doctor)
     */
    private final List<Shorty> patients;
    private final List<Doctor> doctors;

    public Hospital(String name) {
        super(name, 80, 25, true);
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
    }

    public void admitPatient(Shorty patient) {
        if (!patients.contains(patient)) {
            patients.add(patient);
            System.out.printf("%s доставлен в больницу\n", patient.getName());
            try {
                patient.moveTo(this);
            } catch (MovementException e) {
                System.out.println("Ошибка при помещении в больницу: " + e.getMessage());
            }
        }
    }

    public void addDoctor(Doctor doctor) {
        if (!doctors.contains(doctor)) {
            doctors.add(doctor);
            try {
                doctor.moveTo(this);
            } catch (MovementException e) {
                System.out.println("Ошибка при добавлении доктора: " + e.getMessage());
            }
        }
    }
}
