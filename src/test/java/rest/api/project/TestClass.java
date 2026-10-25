package rest.api.project;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TestClass {

    @Test
    public void testRestAssuerdName(){

        Map<String, String> params = new HashMap<>();
        params.put("name", "Andrey");

        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation() // Игнорируем проблемы с SSL-сертификатом
            .queryParams(params)
            //.queryParam("name", "John")
            .when()
            .get("https://playground.learnqa.ru/api/hello")
            .andReturn();

        response.prettyPrint();        

    }

    @Test
    public void testRestAssuredJson(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "Anton");

        JsonPath response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String name = response.get("answer2");
        if (name == null) {
            System.out.println("The key 'answer2' is absent");
        } else {
            System.out.println(name);
        }

    }
}