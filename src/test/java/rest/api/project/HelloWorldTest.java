package rest.api.project;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;
import io.restassured.RestAssured;

public class HelloWorldTest {

    @Test
    public void testHelloWorld(){
        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation() // Игнорируем проблемы с SSL-сертификатом
            .when()
            .get("https://playground.learnqa.ru/api/hello")
            .andReturn();

        response.prettyPrint();        

    }
}
