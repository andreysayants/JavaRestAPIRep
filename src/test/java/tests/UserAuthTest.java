package tests;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;

public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    Map<String, String> authData;

    @BeforeEach 
    public void loginUser(){
        authData = new HashMap<>();
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
        this.authData = authData;
    }

    @Test 
    @DisplayName ("Логин успешный")
    public void testPositiveAuthUser(){

        JsonPath responseCheckAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .header("x-csrf-token", this.header)
            .cookie("auth_sid", this.cookie)
            .get("https://playground.learnqa.ru/api/user/auth")
            .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdOnCheck > 0, "Unexpected user_id " + userIdOnCheck);
        assertEquals(userIdOnAuth, userIdOnCheck, "user_id from auth request is not equal to user_id from check request");
    }

    @ParameterizedTest 
    @ValueSource (strings = {"cookie", "headers"})
    @DisplayName ("Логин не успешный")
    public void testNegativeAuthUser(String condition){
        
        RequestSpecification spec = RestAssured
            .given()
            .relaxedHTTPSValidation();

        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", this.cookie);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", this.header);
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }

        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "user_id should be 0 for unauth request");

    }

    
}
