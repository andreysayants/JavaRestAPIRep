package rest.api.project;

import rest.api.project.Car.Car;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Car myCar = new Car("Tesla", 2025);
        myCar.drive();
        myCar.calculateStatus();
    }
}