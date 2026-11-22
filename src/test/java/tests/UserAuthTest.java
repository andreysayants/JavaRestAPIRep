package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;


@Epic("Authorisation cases")
@Feature("Authorisation")
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach 
    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test 
    @Description ("Успешный логин пользователя с email и password")
    @DisplayName ("Логин успешный")
    public void testPositiveAuthUser(){

        Response responseCheckAuth = apiCoreRequests
            .makeGetRequest(
                "https://playground.learnqa.ru/api/user/auth", 
                this.header, 
                this.cookie);

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @Description ("Не успешный логин пользователя без cookie или headers")
    @DisplayName ("Логин не успешный")
    @ParameterizedTest 
    @ValueSource (strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition){
        
        if (condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                "https://playground.learnqa.ru/api/user/auth", 
                this.cookie);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                "https://playground.learnqa.ru/api/user/auth",
                this.header);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);    
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
    }
}
