package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;

public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach 
    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test 
    @DisplayName ("Логин успешный")
    public void testPositiveAuthUser(){

        Response responseCheckAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .header("x-csrf-token", this.header)
            .cookie("auth_sid", this.cookie)
            .get("https://playground.learnqa.ru/api/user/auth")
            .andReturn();
        
            Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @ParameterizedTest 
    @ValueSource (strings = {"cookie", "headers"})
    @DisplayName ("Логин не успешный")
    public void testNegativeAuthUser(String condition){
        
        RequestSpecification spec = RestAssured
            .given()
            .relaxedHTTPSValidation();

        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        switch (condition) {
            case "cookie" -> spec.cookie("auth_sid", this.cookie);
            case "headers" -> spec.header("x-csrf-token", this.header);
            default -> throw new IllegalArgumentException("Condition value is known: " + condition);
        }

        Response responseForCheck = spec.get().andReturn();
        Assertions.assertJsonByName(responseForCheck, "user_id", 0);

    }

    
}
