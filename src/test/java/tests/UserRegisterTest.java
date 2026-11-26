package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

@Epic("Авторизационные тесты")
@Feature("Регистрация")
public class UserRegisterTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test 
    @DisplayName ("Регистрация пользователя с уже зарегестрированным email")
    public void testCreateUserWithExistinEmail(){
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/",
            userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }

    @Test 
    @Tag("smoke")
    @DisplayName ("Успешная регистрация пользователя")
    public void testCreateUserSuccessfully(){
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/",
            userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);    
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }
}
