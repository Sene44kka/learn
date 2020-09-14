import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class mainClass {
    private static String name;
    private static String last_name;
    private static int age;
    private static Date dateBirthday;
    static TreeMap<Integer, Human> map = new TreeMap<>();
    static TreeMap<Integer, Human> map_serializable = new TreeMap<>();
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static int id;
    static final String FILE_NAME = "dataBase.txt";

    public static void main(String args[]) throws IOException, ParseException {
        boolean flag = true;
        String choice = null;
        int local_id;
        //Получаем значение id для последнего человека, добавленного в эту мапу
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            map = (TreeMap<Integer, Human>) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        //Получаем id+1 последнего человека в TreeMap
        id = getId(map);
        while (flag) {
            System.out.println("Выберите действие:\n-'i' : вставить нового человека\n-'d' : удалить человека\n-'s' : сохранить изменения\n"
                    + "-'e' : Выйти из программы\n-'sh' : показать весь список людей\n-'u' : редактировать человека");
            choice = reader.readLine();

            switch (choice) {
                case "i":
                    //Добавляем нового человека в мапу, которую мы считали из файла. Человек добавляется в конец списка,
                    //так как у нас TreeMap
                    map.put(id, mainClass.create_new_human());
                    System.out.println("Человек добавлен в базу\n");
                    id++;
                    break;
                case "sh":
                    //десериализируем людей в коллекцю map_serializable из файла
                    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                        map_serializable = (TreeMap<Integer, Human>) objectInputStream.readObject();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    if (map_serializable.size() == 0) {
                        System.out.println("База данных пуста\n");
                        break;
                    }
                    //Выводим коллекцию на экран
                    SimpleDateFormat format_vivod = new SimpleDateFormat("E, DD-MM-yyyy");
                    for (Map.Entry<Integer, Human> pair : map_serializable.entrySet()) {
                        System.out.println(pair.getKey() + ". Меня зовут " + " " + pair.getValue().getName() + " " + pair.getValue().getLast_name()
                        + ". Мне " + pair.getValue().getAge() + " лет. Родился(ась) в " + format_vivod.format(pair.getValue().getDate()));
                    }
                    break;
                case "s":
                    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                        objectOutputStream.writeObject(map);
                        System.out.println("Изменения сохранены\n");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case "e":
                    System.out.println("Заверщение программы");
                    reader.close();
                    flag = false;
                    break;
                case "d":
                    if(map.size() == 0){
                        System.out.println("Удалять некого. База данных пуста\n");
                        break;
                    }
                    System.out.println("Введите id человека, которого нужно удалить");
                    local_id = Integer.parseInt(reader.readLine());
                    if (local_id > id - 1){
                        System.out.println("Человека с таким id нет в базе");
                        break;
                    }
                    deleted(local_id);
                    System.out.println("Человек с id " + local_id + " удален");
                    break;
                case "u":
                    if(map.size() == 0){
                        System.out.println("Редактировать некого. База данных пуста\n");
                        break;
                    }
                    System.out.println("Введите id человека, которого нужно отредактировать");
                    local_id = Integer.parseInt(reader.readLine());
                    if (local_id > id - 1){
                        System.out.println("Человека с таким id нет в базе");
                        break;
                    }
                    update(local_id);
                    break;
                default:
                    System.out.println("Выбрана неверная команда\n");
            }

        }
        reader.close();

    }

    //Создаем человека
    public static Human create_new_human() throws IOException, ParseException {
        SimpleDateFormat format_vvod = new SimpleDateFormat("DD.MM.yyyy");
        //try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
        System.out.println("Введите имя человека");
        name = reader.readLine();
        System.out.println("Введите фамилию человека");
        last_name = reader.readLine();
        System.out.println("Введите возраст человека");
        age = Integer.parseInt(reader.readLine());
        System.out.println("Введите дату рождения человека в формате \"DD.MM.YYYY\"");
        dateBirthday = format_vvod.parse(reader.readLine());
        //} catch (Exception e) {
        //    System.out.println(e);
        //}
        return new Human(name, last_name, age, dateBirthday);
    }

    //Если в мапе нет ни одного человека, то id = 1, иначе id = id последнего человека + 1
    public static int getId(TreeMap<Integer, Human> map) {
        int id = 0;
        if (map.size() == 0) {
            id = 1;
        } else {
            id = map.lastKey() + 1;
        }
        return id;
    }
    //Удаляем человека из списка
    public static void deleted(int local_id) throws ClassCastException {
        map.remove(local_id);
        int n = 1;
        TreeMap<Integer, Human> map2 = new TreeMap<>();
        for (Map.Entry<Integer, Human> pair : map.entrySet()) {
            map2.put(n, pair.getValue());
            n++;
        }
        map = map2;
        id = getId(map);
    }
    public static void update(int id) throws IOException, ParseException {
        map.put(id, create_new_human());
    }
}

