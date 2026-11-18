package rest.api.project;

import java.util.List;

public class Test1440 {

    public static void main(String[] args) {
        class Person {

            String name;
            Integer age;

            Person(String name, Integer age) {
                this.name = name;
                this.age = age; }

            String getName() {
                return name; }

            Integer getAge() {
                return age;  }
        }
        List<Person> persons = List.of(
                new Person("Qwe", 55),
                new Person("Asd", 13),
                new Person("Zxc", null),
                new Person("Q.A.Z", 33));
        
        System.out.println("Первый вариант");        
        persons.stream().filter(a -> a.getAge() != null && a.getAge() > 18).forEach(p -> System.out.println(p.getName())); 
        
        System.out.println("Второй вариант");
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getAge() != null && persons.get(i).getAge() > 18) {
                System.out.println(persons.get(i).getName());
            }
            
        }
    }



}
