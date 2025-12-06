package caughterror;

import models.characters.*;
import models.locations.*;


public class Serv {
    public static void demonstrateExceptions() {
        System.out.println("\nРассмотрим исключение\n");

        try {
            Location testLocation = new Location("Тестовая локация", 50, true);
            Shorty testShorty = new Korzhik("Тестовый Коржик", testLocation, 50, 30);
            testShorty.moveTo(null);
        } catch (Exception e) {
            System.out.println("Поймано проверяемое исключение: " + e.getMessage());
        }
        try {
            java.util.List<String> list = new java.util.ArrayList<>();
            String item = list.get(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Поймано unchecked исключение: " + e.getClass().getSimpleName());
            System.out.println("Сообщение: " + e.getMessage());
        }
    }
}
