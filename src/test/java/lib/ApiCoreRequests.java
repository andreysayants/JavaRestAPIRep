package lib;

import static io.restassured.RestAssured.given;

import java.util.Map;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class ApiCoreRequests {

    @Step ("Делаем GET запрос с token и cookie")
    public Response makeGetRequest(String url, String token, String cookie){
        return given()
            .filter(new AllureRestAssured())
            .header(new Header("x-csrf-token", token))
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn();
    }

    @Step ("Делаем GET запрос без token и cookie")
    public Response makeGetRequestWithoutTokenCookie(String url){
        return given()
            .filter(new AllureRestAssured())
            .get(url)
            .andReturn();
    }

    @Step ("Делаем GET запрос только с cookie")
    public Response makeGetRequestWithCookie(String url, String cookie){
        return given()
            .filter(new AllureRestAssured())
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn();
    }

    @Step ("Делаем GET запрос только с token")
    public Response makeGetRequestWithToken(String url, String token){
        return given()
            .filter(new AllureRestAssured())
            .header(new Header("x-csrf-token", token))
            .get(url)
            .andReturn();
    }

    @Step ("Делаем POST запрос c authData")
    public Response makePostRequest(String url, Map<String, String> authData){
        return given()
            .filter(new AllureRestAssured())
            .body(authData)
            .post(url)
            .andReturn();
    }

    @Step ("Делаем PUT запрос с editData")
    public Response makePutRequest(String url, Map<String, String> editData, String header, String cookie){
        return given()
            .filter(new AllureRestAssured())
            .body(editData)
            .header("x-csrf-token",header)
            .cookie("auth_sid",cookie)
            .put(url)
            .andReturn();
    }
}
