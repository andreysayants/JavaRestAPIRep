package rest.api.project.Car;

public class Car {
    // Поля лучше делать private для инкапсуляции
    private String brand;
    private int year;

    public Car(String carBrand, int carYear) {
        this.brand = carBrand;
        this.year = carYear;
    }

    // Метод drive() теперь просто выполняет действие и ничего не возвращает (void)
    public void drive() {
        System.out.println(brand + " едет! Год выпуска: " + year);
    }

    // Если вам действительно нужна была логика с возвратом чисел, 
    // её нужно вынести в метод, возвращающий int (например, calculateStatus)
    public int calculateStatus() {
        try {
            // Какая-то логика, которая может выбросить исключение
            return 10; 
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
            return -1; // Возвращаем код ошибки в случае сбоя
        } finally {

            // Блок finally используется для очистки ресурсов (закрытие файлов, соединений),
            // а не для возврата значений. Писать здесь return НЕ РЕКОМЕНДУЕТСЯ.
            System.out.println("Блок finally выполнился всегда. +");
            return 20;
        }
    }
}


