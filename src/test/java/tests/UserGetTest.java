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

@Epic("Авторизационные тесты")
@Feature("Доступы")
public class UserGetTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test 
    @DisplayName ("Проверка полей для не авторизированного пользователя")
    public void testGetUserDataNotAuth(){

        Response responseUserData = apiCoreRequests
            .makeGetRequestWithoutTokenCookie("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstname");
        Assertions.assertJsonHasNotField(responseUserData, "lastname");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test 
    @Tag("smoke")
    @DisplayName ("Проверка полей для авторизированного пользователя")
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");    
        String cookie = this.getCookie(responseGetAuth, "auth_sid");    

        Response responseUserData = apiCoreRequests
            .makeGetRequest(
                "https://playground.learnqa.ru/api/user/2",
                header, cookie);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }
}
