package tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test 
    @DisplayName ("Изменение пользователя")
    public void testEditJustCreatedUser(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
  
        String userId = this.getStringFromJson(apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData), "id"); 
            
        
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
         
        Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
        .makePutRequest(
            "https://playground.learnqa.ru/api/user/" + userId,
             editData, 
             this.getHeader(responseGetAuth, "x-csrf-token"),
             this.getCookie(responseGetAuth, "auth_sid"));

        //GET 
        Response responseUserData = apiCoreRequests
            .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
             this.getHeader(responseGetAuth, "x-csrf-token"),
             this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", newName);

    }

}
