package models.enums;

import java.util.Random;

public enum BaseAct {
    WALK("Гуляю в парке"),
    WORK("Работаю на заводе"),
    REST("Отдыхаю"),
    EAT("Кушаю плюшки"),
    SPORT("Занимаюсь спортом"),
    READ("Читаю интересную книгу"),
    MUSIC("Слушаю музыку");

    private final String description;

    private BaseAct(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
    public static BaseAct getRandomAct() {
        Random random = new Random();
        BaseAct[] acts = values();
        return acts[random.nextInt(acts.length)];
    }

}
