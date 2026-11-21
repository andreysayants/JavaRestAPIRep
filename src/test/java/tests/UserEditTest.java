package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

public class UserEditTest extends BaseTestCase {
    @Test 
    @DisplayName ("Изменение пользователя")
    public void testEditJustCreatedUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateUser = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .jsonPath();

        String userId = responseCreateUser.getString("id");
        
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
            .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
            .body(editData)
            .put("https://playground.learnqa.ru/api/user/" + userId)
            .andReturn();

        //GET
        Response responseUserData = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .headers("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
            .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
            .get("https://playground.learnqa.ru/api/user/" + userId)  
            .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);

    }

}
