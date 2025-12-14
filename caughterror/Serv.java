package caughterror;

import models.characters.*;
import models.clothes.Clothing;
import models.enums.Size;
import models.enums.TypeClothes;
import models.locations.*;


public class Serv {
    public static void demonstrateExceptions() {
        System.out.println("\nРассмотрим исключение\n");

        try {
            // Тестируем LocationException
            Location testLocation = new Location("Тестовая локация", 50, 10, true);
            Shorty testShorty = new Korzhik("Тестовый Коржик", testLocation, 50, 30);
            testShorty.moveTo(null);
        } catch (Exception e) {
            System.out.println("Поймано проверяемое исключение: " + e.getMessage());

        }
        try {
            // Тестируем ClothingException
            Location testLocation = new Location("Тестовая локация", 50, 10, true);
            Shorty testShorty = new Korzhik("Тестовый Коржик", testLocation, 50, 30);

            Clothing testKorzhikClothes = new Clothing(TypeClothes.BLAZER, Size.NORMAL, "голубой", testShorty);
            testShorty.setClothes(null);

        } catch (Exception e) {
            System.out.println("Поймано проверяемое исключение: " + e.getMessage());
        }

        try {
            // Тестируем RecognitionException
            Location testLocation = new Location("Тестовая локация", 50, 10, true);
            Shorty testShorty1 = new Korzhik("Тестовый Коржик", testLocation, 50, 30);
            Shorty testShorty2 = new Svistulkin("Тестовый Свистулькин", testLocation, 50, 30);

            testShorty1.recognizeChance(null, "");

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
