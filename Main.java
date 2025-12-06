/**
 *
 */

import models.characters.*;
import models.clothes.Clothing;
import models.enums.EmotionalState;
import models.enums.Size;
import models.enums.TypeClothes;
import models.exceptions.MovementException;
import models.locations.*;
import caughterror.Serv;

/**
 * В классах Main, Person и Shorty даны подробные комментарии,
 * в классах покетов locations и clothes комментарии короткие,
 * описывающее принципы и логику работы.
 * Интерфейсы, перечисления и запись не прокомментированы, чтобы не повторять
 * то же самое, что было описано в самых важных классах.
 */
public class Main {
    public static void main(String[] args) {
        try{
            /**
             * Создаем локации: дома для каждого героя и отдельно - больницу, так как у нее указаны дополнительный методы.
             * Каждая локация обладает названием, освещением и публинчостью.
             */
            Location korzhikHome = new Location("Квартира Коржика", 20, false);
            Location kompressikHome = new Location("Квартира Доктора Компрессика", 50, false);
            Location makovkaHome = new Location("Квартира Маковки", 80, false);
            Location park = new Location("Парк", 60, true);
            Location street = new Location("Улица", 70, true);
            Location hospital = new Hospital("Больница");

            /**
             * Инициализируем доктора, указываем его имя, начальную локацию и его опыт (влияет на длительность лечения).
             * Инициализируем персонажей, указываем имя, начальную локацию,
             * внимательность (влияет на то, увидят ли герои Свистулькина),
             * сонливость (влияет на возможность Свистулькина заснуть в квартире Коржика).
             */
            Doctor docKompressik = new Doctor("Доктор Компрессик", kompressikHome, 90);
            Korzhik korzhik = new Korzhik("Коржик", street, 60, 20);
            Shutilo shutilo = new Shutilo("Шутило", street, 20, 30);
            Svistulkin svistulkin = new Svistulkin("Свистулькин", park, 30, 100);
            Makovka makovka = new Makovka("Маковка", makovkaHome);

            /**
             * Создаем одежду для героев с указанием типа, размера, цвета, принадлежности.
             * @see TypeClothes - перечисления видов одежды
             * @see Size - перечисление размеров
             */
            Clothing korzhikClothes = new Clothing(TypeClothes.BLAZER, Size.NORMAL, "голубой", korzhik);
            Clothing svistulkinClothes = new Clothing(TypeClothes.COAT, Size.SMALL, "розовое", svistulkin);

            /**
             * Назначаем созданную одежду соответствующим персонажам.
             * @see models.interfaces.ClothingWear
             */
            korzhik.setClothes(korzhikClothes);
            svistulkin.setClothes(svistulkinClothes);

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


            //начало истории
            System.out.println("\nНачнем нашу историю!\n");

            /**
             * Пытаемся переместиться в локацию,
             * если только она существует.
             * @throws MovementException если перемещение невозможно
             *  @see Svistulkin#moveTo(Location)
             */
            try{
                svistulkin.moveTo(korzhikHome);
            } catch (MovementException e) {
                System.out.println("Error: " + e.getMessage());
            }

            /**
             * Определяем по каким из двух сценариев будем действовать дальше,
             * в зависимости от того, удалось ли Свистулькину уснуть.
             * @see Svistulkin#maybeSleep(Location)
             */
            boolean sleepInHome = svistulkin.maybeSleep(korzhikHome);

            /**
             * Если Свистулькин заснул в квартире Коржика.
             * @see sleepInHome - флаг
             */
            if (sleepInHome) {
                /**
                 * Оба персонажа пытаются переместиться в одну локацию.
                 * @throws MovementException если перемещение невозможно
                 * @see Korzhik#moveTo(Location)
                 * @see Shutilo#moveTo(Location)
                 */
                try {
                    korzhik.moveTo(korzhikHome);
                    shutilo.moveTo(korzhikHome);
                } catch (MovementException e) {
                    System.out.println("Error: " + e.getMessage());
                }

                /**
                 * Проверяем, заметили ли персонажи Свистулькина.
                 * Зависит от уровня внимательности каждого персонажа.
                 * @see Korzhik#getAttentiveness()
                 * @see Shutilo#getAttentiveness()
                 */
                boolean sight_Korzhik = korzhik.getAttentiveness() > 50;
                boolean sight_Shutilo = shutilo.getAttentiveness() > 50;
                boolean sight = sight_Shutilo || sight_Korzhik;

                /**
                 * Изменяется эмоциональное состояние в зависимости от исхода.
                 * @see EmotionalState#AFRAID Состояние испуга
                 * @see EmotionalState#CONFUSED Состояние замешательства
                 * @see Clothing#getDescription() Описание пропавшей одежды
                 */
                if (sight) {
                    System.out.println("Малыши заметили спящего Свистулькина");
                    korzhik.speak("А! Кто-то спит в нашей квартире!");
                    korzhik.setEmotionalState(EmotionalState.AFRAID);
                } else {
                    System.out.println("Малыши не заметили спящего Свистулькина");
                    korzhik.speak("Так странно, мой/я " + korzhikClothes.getDescription() + " размера пропал/a");
                    korzhik.setEmotionalState(EmotionalState.CONFUSED);
                }

                /**
                 * Просыпается и уходит.
                 * @see Svistulkin#wakeUp() Метод пробуждения
                 */
                svistulkin.wakeUp();

                /**
                 * Проверяем не перепутал ли одежду Свистулькин.
                 * Вероятность ошибки зависит от внимательности персонажа.
                 * @see Svistulkin#maybeWearWrong(Clothing, Clothing)
                 */
                boolean mbWrong = svistulkin.maybeWearWrong(korzhikClothes, svistulkinClothes);

                /**
                 * Определяем, в чьей одежде уходит Свистулькин
                 * @see Clothing#getDescription() Для отображения описания одежды
                 */
                if (mbWrong){
                    System.out.println("Свистулькин ушел в чужой/ом " + korzhikClothes.getDescription() + " размера");
                } else {
                    System.out.println("Свистулькин ушел в своей/ом " + svistulkinClothes.getDescription()+ " размера");
                }
                try {
                    svistulkin.moveTo(street);
                } catch (MovementException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            /**
             * Свистулькин НЕ уснул в квартире.
             * @see sleepInHome Флаг, активирующий эту ветку
             */
            if (!sleepInHome) {
                System.out.println("Свистулькин выпил кофе и собирается уходить)");
                /**
                 * Свистулькин становится счастливым
                 * @see EmotionalState#HAPPY Состояние счастья
                 * @see Svistulkin#setEmotionalState(EmotionalState)
                 */
                svistulkin.setEmotionalState(EmotionalState.HAPPY);
                try {
                    korzhik.moveTo(korzhikHome);
                    shutilo.moveTo(korzhikHome);
                } catch (MovementException e) {
                    System.out.println("Error: " + e.getMessage());
                }

                /**
                 * Аналогичные действия, что и в предыдущем сценарии.
                 */
                boolean sight_Korzhik = korzhik.getAttentiveness() > 50;
                boolean sight_Shutilo = shutilo.getAttentiveness() > 50;
                boolean sight = sight_Shutilo || sight_Korzhik;

                if (sight) {
                    System.out.println("Малыши заметили Свистулькина на кухне");
                    korzhik.speak("А! Кто-то пьет наш кофе!");
                    korzhik.setEmotionalState(EmotionalState.AFRAID);
                } else {
                    System.out.println("Малыши не заметили Свистулькина на кухне");
                    korzhik.speak("Так странно, мой/я " + korzhikClothes.getDescription() + " размера пропал/a");
                    korzhik.setEmotionalState(EmotionalState.CONFUSED);
                }

                boolean mbWrong = svistulkin.maybeWearWrong(korzhikClothes, svistulkinClothes);

                if (mbWrong) {
                    System.out.println("Свистулькин ушел в чужой/ом " + korzhikClothes.getDescription()+ " размера");
                } else {
                    System.out.println("Свистулькин ушел в своей/ом " + svistulkinClothes.getDescription() + "размера");
                }
                try {
                    svistulkin.moveTo(street);
                } catch (MovementException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            /**
             * Заболевает.
             * @see Svistulkin#getSick() Метод изменения состояния здоровья
             */
            svistulkin.getSick();

            try {
                makovka.moveTo(street);
                System.out.println("Малышка Маковка находит больного Свистулькина на улице");
                makovka.speak("Надо отвезти больного малыша в больницу!");
            } catch (MovementException e){
                System.out.println("Error: " + e.getMessage());
            }

            /**
             * Госпитализируем Свистулькина.
             * Приведение типа Location к Hospital для доступа к медицинским методам.
             * @see Hospital#admitPatient(Shorty) Приём пациента в больницу
             * @see Hospital#addDoctor(Doctor) Добавление врача
             */
            Hospital hospitalObj = (Hospital) hospital;
            hospitalObj.admitPatient(svistulkin);
            hospitalObj.addDoctor(docKompressik);
            /**
             * Диагностика и озвучивание диагноза.
             * @see Doctor#diagnose(Shorty) Метод диагностики
             * @see Doctor#speak(String) Озвучивание диагноза
             */
            String treatment = docKompressik.diagnose(svistulkin);
            docKompressik.speak(treatment);

            /**
             * Перемещение персонажей в больницу с вещами Свистклькина.
             * Разговоры о "госте"
             */
            try {
                korzhik.moveTo(hospital);
                shutilo.moveTo(hospital);
                shutilo.setClothes(svistulkinClothes);
                System.out.println("Шутило взял с собой " + svistulkinClothes.getDescription() + " размера Свистулькина");
            } catch (MovementException e) {
                System.out.println("Ошибка при перемещении: " + e.getMessage());
            }

            docKompressik.tellStory("про малыша, которого доставила к нам в больницу малышка Маковка", korzhik);
            docKompressik.tellStory("про малыша, которого доставила к нам в больницу малышка Маковка", shutilo);
            /**
             * В зависимости от ранее произошедшего сценария,
             * персонажи выдают разные фразы.
             */
            if (sleepInHome) {
                korzhik.tellStory("про малыша, который неизвестно каким образом заснул у них дома", docKompressik);
                shutilo.speak("Он наверное надел чужую/ой " + korzhikClothes.getDescription() + " размера!");
            } else {
                korzhik.speak("Странно, у нас пропал/а " + korzhikClothes.getDescription() + ", но мы не знаем почему...");
                korzhik.setEmotionalState(EmotionalState.SAD);
            }

            /**
             * Попытка узнать Свистулькина,
             * зависит от шанса распознавания и контекста.
             * @see Korzhik#recognizeChance(Shorty, String) Шанс распознавания Коржика
             * @see Shutilo#recognizeChance(Shorty, String) Шанс распознавания Шутило
             */
            boolean korzhikRecognized = korzhik.recognizeChance(svistulkin, "hospital");
            boolean shutiloRecognized = shutilo.recognizeChance(svistulkin, "hospital");

            /**
             * Если персонажи узнали Свистулькина, то они узнают личность гостя,
             * забирают обратно свои вещи.
             * @see Clothing#equals(Object) Сравнение объектов одежды
             * @see Clothing#changePerson(Person) Смена владельца одежды
             */
            if (korzhikRecognized || shutiloRecognized) {
                System.out.println("Герои узнали Свистулькина!");
                korzhik.speak("Это тот самый коротышка, которого мы застали у себя ночью дома!");

                if (svistulkin.getClothes() != null && svistulkin.getClothes().equals(korzhikClothes)) {
                    System.out.println("И он действительно в " + korzhikClothes.getDescription() + " Коржика!");
                    korzhik.setClothes(korzhikClothes);
                    korzhikClothes.changePerson(korzhik);
                    svistulkin.setClothes(null);
                    System.out.println("Коржик забрал свою "+ korzhikClothes.getDescription()+ " размера");
                }

                /**
                 * Рассчитываем длительность лечения для визита.
                 * @see Doctor#calculateDays(Shorty) Расчёт длительности лечения
                 */
                int visitDays = docKompressik.calculateDays(svistulkin);
                korzhik.speak("Можно мы навестим больного через " + visitDays + " день(дня/дней) и расспросим его поподробнее?");
                docKompressik.speak("Конечно, приходите!");

            } else {
                /**
                 * Если не узнали, то персонажи не узнают личность похитителя одежды.
                 * @see EmotionalState#SAD Установка состояния грусти
                 */
                System.out.println("Герои не узнали Свистулькина...");
                korzhik.setEmotionalState(EmotionalState.SAD);
                shutilo.setEmotionalState(EmotionalState.SAD);
                korzhik.speak("Жаль, мы так и не нашли того, кто взял мою куртку...");
            }

            /**
             * Персонажи возвращаются домой, Свистулькина лечат в больнице.
             * @see Korzhik#moveTo(Location)
             * @see Shutilo#moveTo(Location)
             * @see Doctor#treat(Shorty) Метод лечения пациента
             */
            try {
                korzhik.moveTo(korzhikHome);
                shutilo.moveTo(korzhikHome);
            } catch (MovementException e) {
                System.out.println("Ошибка при перемещения: " + e.getMessage());
            }

            docKompressik.treat(svistulkin);


            /**
             * В любой момент истории можно посмотреть память.
             */
            /*System.out.println("###Проверка памяти###");
            korzhik.printMemory();
            svistulkin.printMemory();*/
            //конец
            /**
             * Показывается обработка исключения.
             * @see Serv#demonstrateExceptions() Демонстрационный метод
             */
            Serv.demonstrateExceptions();

        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();

        }
    }
}