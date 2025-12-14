import models.characters.*;
import models.clothes.Clothing;
import models.enums.EmotionalState;
import models.enums.Size;
import models.enums.TypeClothes;
import models.exceptions.ClothingException;
import models.exceptions.MovementException;
import models.exceptions.RecognizeException;
import models.locations.*;
import caughterror.Serv;

/**
 * В классах Main, Person, Shorty и Doctor даны подробные комментарии,
 * в классах покетов locations и clothes комментарии короткие,
 * описывающее принципы и логику работы.
 * Интерфейсы, перечисления и запись не прокомментированы, чтобы не повторять
 * то же самое, что было описано в самых важных классах.
 * <p>
 * Показывается обработка исключения.
 * @see Serv#demonstrateExceptions() Демонстрационный метод
 */

public class Main {
    public static void main(String[] args) {
        try {
            fullStory();
            Serv.demonstrateExceptions();

        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void fullStory() throws Exception {
        StoryWorld world = settingsForWorld();
        boolean sleepVar = act1_Introduction(world);
        act2_Hospital(world, sleepVar);
        act3_Recognition(world, sleepVar);
        act4_Conclusion(world);
    }
    /**
     * Создаем локации: дома для каждого героя и отдельно - больницу, так как у нее указаны дополнительный методы.
     * Каждая локация обладает названием, освещением, публинчостью и температурой, от которой зависит тяжесть болезни Свистулькина.
     * <p>
     * Инициализируем доктора, указываем его имя, начальную локацию и его опыт (влияет на длительность лечения).
     * Инициализируем персонажей, указываем имя, начальную локацию, внимательность (влияет на то, увидят ли герои Свистулькина),
     * сонливость (влияет на возможность Свистулькина заснуть в квартире Коржика).
     * <p>
     * Создаем одежду для героев с указанием типа, размера, цвета, принадлежности.
     * @see TypeClothes - перечисления видов одежды
     * @see Size - перечисление размеров
     */
    private static StoryWorld settingsForWorld() {
        // Создаем локации
        Location korzhikHome = new Location("Квартира Коржика", 20, 22,false);
        Location kompressikHome = new Location("Квартира Доктора Компрессика", 50, 25, false);
        Location makovkaHome = new Location("Квартира Маковки", 80, 24, false);
        Location park = new Location("Парк", 60, -10, true);
        Location street = new Location("Улица", 70, -15, true);
        Location hospital = new Hospital("Больница");

        // Создаем персонажи
        Doctor docKompressik = new Doctor("Доктор Компрессик", kompressikHome, 90);
        Korzhik korzhik = new Korzhik("Коржик", street, 60, 20);
        Shutilo shutilo = new Shutilo("Шутило", street, 20, 30);
        Svistulkin svistulkin = new Svistulkin("Свистулькин", park, 30, 100);
        Makovka makovka = new Makovka("Маковка", makovkaHome);

        // Создаем одежду
        Clothing korzhikClothes = new Clothing(TypeClothes.BLAZER, Size.NORMAL, "голубой", korzhik);
        Clothing svistulkinClothes = new Clothing(TypeClothes.COAT, Size.SMALL, "розовое", svistulkin);

        // Одеваем персонажей
        dressCharacters(korzhik, korzhikClothes, svistulkin, svistulkinClothes);

        // Знакомство с персонажами
        introduceCharacters(korzhik, shutilo, svistulkin, makovka, docKompressik);

        return new StoryWorld(
                korzhikHome, kompressikHome, makovkaHome, park, street, hospital,
                docKompressik, korzhik, shutilo, svistulkin, makovka,
                korzhikClothes, svistulkinClothes);
    }

    private static void dressCharacters(Korzhik korzhik, Clothing korzhikClothes, Svistulkin svistulkin, Clothing svistulkinClothes) {
        try {
            korzhik.setClothes(korzhikClothes);
        } catch (ClothingException e) {
            System.out.println("Error korzhik: " + e.getMessage());
        }
        try {
            svistulkin.setClothes(svistulkinClothes);
        } catch (ClothingException e) {
            System.out.println("Error svistulkin: " + e.getMessage());
        }
    }
    private static void introduceCharacters(Korzhik korzhik, Shutilo shutilo, Svistulkin svistulkin, Makovka makovka, Doctor docKompressik) {
        System.out.println("Знакомство с персонажами\n");
        System.out.println(korzhik);
        korzhik.baseAction();
        System.out.println(shutilo);
        shutilo.baseAction();
        System.out.println(svistulkin);
        svistulkin.baseAction();
        System.out.println(makovka);
        makovka.baseAction();
        System.out.println(docKompressik);
        docKompressik.baseAction();
    }

    /**
     * Пытаемся переместиться в локацию,
     * если только она существует.
     * @see Svistulkin#moveTo(Location)
     * <p>
     * Определяем по каким из двух сценариев будем действовать дальше,
     * в зависимости от того, удалось ли Свистулькину уснуть.
     * @see Svistulkin#maybeSleep(Location)
     */
    private static boolean act1_Introduction(StoryWorld world){
        //начало истории
        System.out.println("\nНачнем нашу историю!\n");

        try{
            world.svistulkin.moveTo(world.korzhikHome);
        } catch (MovementException e) {
            System.out.println("Error: " + e.getMessage());
        }

        boolean sleepInHome = world.svistulkin.maybeSleep(world.korzhikHome);
        if (sleepInHome) {
            SleepVar(world);
        } else {
            notSleepVar(world);
        }
        return sleepInHome;
    }

    /**
     * Если Свистулькин заснул в квартире Коржика.
     * @see #act1_Introduction(StoryWorld)
     * Оба персонажа пытаются переместиться в одну локацию.
     * @see Korzhik#moveTo(Location)
     * @see Shutilo#moveTo(Location)
     * <p>
     * Проверяем, заметили ли персонажи Свистулькина.
     * Зависит от уровня внимательности каждого персонажа.
     * @see Korzhik#getAttentiveness()
     * @see Shutilo#getAttentiveness()
     * <p>
     * Изменяется эмоциональное состояние в зависимости от исхода.
     * @see EmotionalState#AFRAID Состояние испуга
     * @see EmotionalState#CONFUSED Состояние замешательства
     * @see Clothing#getDescription() Описание пропавшей одежды
     * <p>
     * Просыпается и уходит.
     * @see #svistulkinGoAway(StoryWorld, boolean)
     */
    private static void SleepVar(StoryWorld world){
        try {
            world.korzhik.moveTo(world.korzhikHome);
            world.shutilo.moveTo(world.korzhikHome);
        } catch (MovementException e) {
            System.out.println("Error: " + e.getMessage());
        }

        boolean sight = checkIfNoticed(world.korzhik, world.shutilo);

        if (sight) {
            System.out.println("Малыши заметили спящего Свистулькина");
            world.korzhik.speak("А! Кто-то спит в нашей квартире!");
            world.korzhik.setEmotionalState(EmotionalState.AFRAID);
        } else {
            System.out.println("Малыши не заметили спящего Свистулькина");
            world.korzhik.speak("Так странно, мой/я " + world.korzhikClothes.getDescription() + " размера пропал/a");
            world.korzhik.setEmotionalState(EmotionalState.CONFUSED);
        }

        svistulkinGoAway(world, true);
    }

    /**
     * Свистулькин НЕ уснул в квартире.
     * @see #act1_Introduction(StoryWorld)
     * <p>
     * Свистулькин становится счастливым
     * @see EmotionalState#HAPPY Состояние счастья
     * @see Svistulkin#setEmotionalState(EmotionalState)
     */
    private static void notSleepVar(StoryWorld world){
        System.out.println("Свистулькин выпил кофе и собирается уходить)");
        world.svistulkin.setEmotionalState(EmotionalState.HAPPY);
        try {
            world.korzhik.moveTo(world.korzhikHome);
            world.shutilo.moveTo(world.korzhikHome);
        } catch (MovementException e) {
            System.out.println("Error: " + e.getMessage());
        }
        boolean sight = checkIfNoticed(world.korzhik, world.shutilo);
        if (sight) {
            System.out.println("Малыши заметили Свистулькина на кухне");
            world.korzhik.speak("А! Кто-то пьет наш кофе!");
            world.korzhik.setEmotionalState(EmotionalState.AFRAID);
        } else {
            System.out.println("Малыши не заметили Свистулькина на кухне");
            world.korzhik.speak("Так странно, мой/я " + world.korzhikClothes.getDescription() + " размера пропал/a");
            world.korzhik.setEmotionalState(EmotionalState.CONFUSED);
        }
        svistulkinGoAway(world, false);
    }

    /**
     * Проверяем, заметили ли персонажи Свистулькина.
     * Зависит от уровня внимательности каждого персонажа.
     * @see Korzhik#getAttentiveness()
     * @see Shutilo#getAttentiveness()
     */
    private static boolean checkIfNoticed(Korzhik korzhik, Shutilo shutilo) {
        boolean sight_Korzhik = korzhik.getAttentiveness() > 50;
        boolean sight_Shutilo = shutilo.getAttentiveness() > 50;
        return sight_Shutilo || sight_Korzhik;
    }

    /**
     * Просыпается и уходит.
     * @see Svistulkin#wakeUp() Метод пробуждения
     * <p>
     * Проверяем не перепутал ли одежду Свистулькин.
     * Вероятность ошибки зависит от внимательности персонажа.
     * @see Svistulkin#maybeWearWrong(Clothing, Clothing)
     * <p>
     * Определяем, в чьей одежде уходит Свистулькин
     * @see Clothing#getDescription() Для отображения описания одежды
     */
    private static void svistulkinGoAway(StoryWorld world, boolean wasSleeping) {
        if (wasSleeping) {
            world.svistulkin.wakeUp();
        }

        boolean mbWrong = world.svistulkin.maybeWearWrong(world.korzhikClothes, world.svistulkinClothes);

        if (mbWrong){
            System.out.println("Свистулькин ушел в чужой/ом " + world.korzhikClothes.getDescription() + " размера");
        } else {
            System.out.println("Свистулькин ушел в своей/ом " + world.svistulkinClothes.getDescription()+ " размера");
        }
        try {
            world.svistulkin.moveTo(world.street);
        } catch (MovementException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Заболевает.
     * Вызывается метод определения степени тяжести заболевания.
     * Зависет от температуры.
     * @see Location#degreeOfIllness(Shorty)
     * <p>
     * Госпитализируем Свистулькина.
     * Приведение типа Location к Hospital для доступа к медицинским методам.
     * @see Hospital#admitPatient(Shorty) Приём пациента в больницу
     * @see Hospital#addDoctor(Doctor) Добавление врача
     * <p>
     * Диагностика и озвучивание диагноза.
     * @see Doctor#diagnose(Shorty) Метод диагностики
     * @see Doctor#speak(String) Озвучивание диагноза
     * <p>
     * Выводим данные мед крижки
     * @see Doctor#printMedHistory(Shorty)
     */
    private static void act2_Hospital(StoryWorld world, boolean sleepVar) {
        world.street.degreeOfIllness(world.svistulkin);
        try {
            world.makovka.moveTo(world.street);
            System.out.println("Малышка Маковка находит больного Свистулькина на улице");
            world.makovka.speak("Надо отвезти больного малыша в больницу!");
        } catch (MovementException e){
            System.out.println("Error: " + e.getMessage());
        }

        // Госпитализация
        Hospital hospitalObj = (Hospital) world.hospital;
        hospitalObj.admitPatient(world.svistulkin);
        hospitalObj.addDoctor(world.docKompressik);
        // Диагностика
        String treatment = world.docKompressik.diagnose(world.svistulkin);
        world.docKompressik.speak(treatment);

        // Выводим данные из медкнижки
        world.docKompressik.printMedHistory(world.svistulkin);

        // Коржик и Шутило приходят в больницу
        inHospital(world, sleepVar);
    }

    /**
     * Перемещение персонажей в больницу с вещами Свистклькина.
     * Разговоры о "госте"
     * <p>
     * В зависимости от ранее произошедшего сценария,
     * персонажи выдают разные фразы.
     */
    private static void inHospital(StoryWorld world, boolean sleepVar) {
        try {
            world.korzhik.moveTo(world.hospital);
            world.shutilo.moveTo(world.hospital);
            try {
                world.shutilo.setClothes(world.svistulkinClothes);
                System.out.println("Шутило взял с собой " + world.svistulkinClothes.getDescription() + " размера Свистулькина");
            } catch (ClothingException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (MovementException e) {
            System.out.println("Ошибка при перемещении: " + e.getMessage());
        }

        world.docKompressik.tellStory("про малыша, которого доставила к нам в больницу малышка Маковка", world.korzhik);
        world.docKompressik.tellStory("про малыша, которого доставила к нам в больницу малышка Маковка", world.shutilo);

        if (sleepVar) {
            world.korzhik.tellStory("про малыша, который неизвестно каким образом заснул у них дома", world.docKompressik);
            world.shutilo.speak("Он наверное надел чужую/ой " + world.korzhikClothes.getDescription() + " размера!");
        } else {
            world.korzhik.speak("Странно, у нас пропал/а " + world.korzhikClothes.getDescription() + ", но мы не знаем почему...");
            world.korzhik.setEmotionalState(EmotionalState.SAD);
        }
    }

    /**
     * Попытка узнать Свистулькина,
     * зависит от шанса распознавания и контекста.
     * @see Korzhik#recognizeChance(Person, String)  Шанс распознавания Коржика
     * @see Shutilo#recognizeChance(Person, String)  Шанс распознавания Шутило
     */
    private static void act3_Recognition(StoryWorld world, boolean sleepVar) {
        boolean korzhikRecognized = attemptRecognition(world.korzhik, world.svistulkin, "hospital");
        boolean shutiloRecognized = attemptRecognition(world.shutilo, world.svistulkin, "hospital");

        if (korzhikRecognized || shutiloRecognized){
            successRecognition(world);
        } else{
            failRecognition(world);
        }
    }

    private static boolean attemptRecognition(Shorty recognizer, Shorty other, String context){
        try {
            return recognizer.recognizeChance(other, context);
        } catch (RecognizeException e) {
            System.out.println("Ошибка распознавания: " + e.getMessage());
            return false;
        }
    }
    /**
     * Если персонажи узнали Свистулькина, то они узнают личность гостя,
     * забирают обратно свои вещи.
     * @see Clothing#equals(Object) Сравнение объектов одежды
     * @see Clothing#changePerson(Person) Смена владельца одежды
     * <p>
     * Рассчитываем длительность лечения для визита.
     * @see Doctor#calculateDays(Shorty) Расчёт длительности лечения
     */
    private static void successRecognition(StoryWorld world){
        System.out.println("Герои узнали Свистулькина!");
        world.korzhik.speak("Это тот самый коротышка, которого мы застали у себя ночью дома!");

        if (world.svistulkin.getClothes().equals(world.korzhikClothes)) {
            System.out.println("И он действительно в " + world.korzhikClothes.getDescription() + " Коржика!");
            try {
                world.korzhik.setClothes(world.korzhikClothes);
            } catch (ClothingException e) {
                System.out.println("Error: " + e.getMessage());
            }
            world.korzhikClothes.changePerson(world.korzhik);

            try {
                world.svistulkin.setClothes(world.svistulkinClothes);
                System.out.println("Коржик забрал свой/ю " + world.korzhikClothes.getDescription() + " размера");
            } catch (ClothingException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
        // Длительность лечения
        int visitDays = world.docKompressik.calculateDays(world.svistulkin);
        world.korzhik.speak("Можно мы навестим больного через " + visitDays + " день(дня/дней) и расспросим его поподробнее?");
        world.docKompressik.speak("Конечно, приходите!");
    }

    /**
     * Если не узнали, то персонажи не узнают личность похитителя одежды.
     * @see EmotionalState#SAD Установка состояния грусти
     */
    private static void failRecognition(StoryWorld world){
        System.out.println("Герои не узнали Свистулькина...");
        world.korzhik.setEmotionalState(EmotionalState.SAD);
        world.shutilo.setEmotionalState(EmotionalState.SAD);
        world.korzhik.speak("Жаль, мы так и не нашли того, кто взял мою куртку...");
    }

    /**
     * Персонажи возвращаются домой, Свистулькина лечат в больнице.
     * @see Korzhik#moveTo(Location)
     * @see Shutilo#moveTo(Location)
     * @see Doctor#treat(Shorty) Метод лечения пациента
     */
    private static void act4_Conclusion(StoryWorld world){
        try {
            world.korzhik.moveTo(world.korzhikHome);
            world.shutilo.moveTo(world.korzhikHome);
        } catch (MovementException e) {
            System.out.println("Ошибка при перемещения: " + e.getMessage());
        }
        world.docKompressik.treat(world.svistulkin);
        // В любой момент истории можно посмотреть память.
        /*System.out.println("###Проверка памяти###");
        korzhik.printMemory();
        svistulkin.printMemory();*/
    }

    private record StoryWorld(Location korzhikHome, Location kompressikHome, Location makovkaHome, Location park,
                              Location street, Location hospital, Doctor docKompressik, Korzhik korzhik,
                              Shutilo shutilo, Svistulkin svistulkin, Makovka makovka, Clothing korzhikClothes,
                              Clothing svistulkinClothes) {
    }
}