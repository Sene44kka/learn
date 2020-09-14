import java.io.Serializable;
import java.util.Date;

public class Human implements Serializable { //Класс человек

    private String name;
    private String last_name;
    private int age;
    private Date date;

    public Human(String name, String last_name, int age, Date date){
        this.name = name;
        this.last_name = last_name;
        this.age = age;
        this.date = date;
    }
    public String getName(){
        return name;
    }
    public String getLast_name(){
        return last_name;
    }
    public int getAge(){
        return age;
    }
    public Date getDate(){
        return date;
    }
}
