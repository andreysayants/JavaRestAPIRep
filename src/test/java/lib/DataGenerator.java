package lib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataGenerator {
    public static String getRandomEmail(){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "learnqa" + timestamp + "@example.com";
    }
}
