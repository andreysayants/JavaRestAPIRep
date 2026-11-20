package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;

public class UserGetTest extends BaseTestCase {
    @Test 
    @DisplayName ("Проверка полей для не авторизированного пользователя")
    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstname");
        Assertions.assertJsonHasNotField(responseUserData, "lastname");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test 
    @DisplayName ("Проверка полей для авторизированного пользователя")
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");    
        String cookie = this.getCookie(responseGetAuth, "auth_sid");    

        Response responseUserData = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .header("x-csrf-token",header)
            .cookie("auth_sid", cookie)
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

}
