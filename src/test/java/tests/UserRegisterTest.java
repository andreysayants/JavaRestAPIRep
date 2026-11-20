package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

public class UserRegisterTest extends BaseTestCase {
    @Test 
    @DisplayName ("Регистрация пользователя с уже зарегестрированным email")
    public void testCreateUserWithExistinEmail(){
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }

    @Test 
    @DisplayName ("Успешная регистрация пользователя")
    public void testCreateUserSuccessfully(){
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);    
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }
}
