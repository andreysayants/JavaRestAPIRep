package rest.api.project;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TestClass {

    @Test
    @DisplayName ("Response")
    public void testRAResponse(){

        Map<String, String> params = new HashMap<>();
        params.put("name", "Andrey");

        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation() // Игнорируем проблемы с SSL-сертификатом
            .queryParams(params)
            //.queryParam("name", "John")
            .when()
            .get("https://playground.learnqa.ru/api/hello")
            .andReturn();

        assertEquals(200, response.statusCode(), "Unexpected status code");

        response.prettyPrint();        

    }

    @Test
    @DisplayName ("Json")
    public void testRAJson(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "Anton");

        JsonPath response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .queryParams(params)
            .get("https://playground.learnqa.ru/api/hello")
            .jsonPath();

        String name = response.get("answer2");
        if (name == null) {
            System.out.println("The key 'answer2' is absent");
        } else {
            System.out.println(name);
        }

        assertEquals(null, name, "Введен верный атрибут answer");

    }

    @ParameterizedTest 
    @ValueSource(strings = {"", "Anton", "Andrey"})
    @DisplayName ("Параметризированный тест")
    public void testRAParametrized(String name){
        Map<String, String> params = new HashMap<>();

        if(name.length()>0){
            params.put("name", name);
        }

        JsonPath response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .queryParams(params)
            .when()
            .get("https://playground.learnqa.ru/api/hello")
            .jsonPath();

        String answer = response.getString("answer");
        String expectedName = (name.length()>0) ? name : "someone";
        assertEquals("Hello, " + expectedName, answer, "The answer is not expected");
    }



    @Test 
    @DisplayName ("Методы GET")
    public void testRATypeGet(){
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.put("param1", "value1");
        parametersMap.put("param2", "value2");

        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .queryParams(parametersMap)
            .get("https://playground.learnqa.ru/api/check_type")
            .andReturn();

        response.print();
    }

    @Test 
    @DisplayName ("Метод POST")
    public void testRATypePost(){
        Map<String, Object> body = new HashMap<>();
        body.put("params1", "value1");
        body.put("params2", "value2");
        body.put("params3", "value3");

        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(body)
            .post("https://playground.learnqa.ru/api/check_type")
            .andReturn();

        response.print();
    }

    @Test 
    @DisplayName("Код ответа 500") 
    public void testRACodesSE(){
        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .get("https://playground.learnqa.ru/api/get_500")
            .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test 
    @DisplayName ("Код ответа 303")
    public void testRACodesRedirect(){
        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .redirects()
            .follow(false)
            .when() //не обязательный, синтаксический сахар
            .get("https://playground.learnqa.ru/api/get_303")
            .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);    
    }

    @Test 
    @DisplayName ("Headers")
    public void testRAHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeaders1", "myValue1");
        headers.put("myHeaders2", "myValue2");
        headers.put("myHeaders3", "myValue3");

        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .headers(headers)
            .when()
            .get("https://playground.learnqa.ru/api/show_all_headers")
            .andReturn();

        response.prettyPrint();    

        Headers respoHeaders = response.getHeaders();
        System.out.println("Заголовки, которые мы получили в ответе \n" + respoHeaders);
    }

    @Test 
    @DisplayName ("Получаем Location куда будет редирект")
    public void testRARedirectLocation(){
        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .redirects()
            .follow(false)
            .when()
            .get("https://playground.learnqa.ru/api/get_303")
            .andReturn();

        response.prettyPrint();  

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);

        Headers responseHeaders = response.getHeaders();
        System.out.println("Заголовки, которые мы получили в ответе \n" + responseHeaders);
    }

    @Test
    @DisplayName("Wrong Cookies")
    public void testRAWrongCookies(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login3");
        data.put("password", "secret_pass3");

        Response response = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(data)
            .when()
            .post("https://playground.learnqa.ru/api/get_auth_cookie")
            .andReturn();

        System.out.println("\n Красивый текст: ");
        response.prettyPrint();

        System.out.println("\n Headers: ");
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\n Cookies: ");
        Map<String, String> responseCookies = response.getCookies();
        System.out.println(responseCookies);
    }

    @Test 
    @DisplayName("Передать Cookie") 
    public void testRASetCookie(){
        Map<String, String> datMap = new HashMap<>();
        datMap.put("login", "secret_login1");
        datMap.put("password", "secret_pass");

        Response responseForGet = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(datMap)
            .when()
            .post("https://playground.learnqa.ru/api/get_auth_cookie")
            .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");
        
        Map<String, String> cookies = new HashMap<>();
        if(responseCookie != null){  // You are authorized если логин верный и You are NOT authorized, если логин не верный
            cookies.put("auth_cookie", responseCookie);
        }

        Response responseForCheck = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(datMap)
            .cookies(cookies)
            .when()
            .post("https://playground.learnqa.ru/api/check_auth_cookie")
            .andReturn();

        responseForCheck.print();    
    }

    @Test 
    @DisplayName ("Логин успешный")
    public void testAuthUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();

        Map<String, String> cookies = responseGetAuth.getCookies();  
        Headers headers = responseGetAuth.getHeaders();
        int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");

        assertEquals(200, responseGetAuth.getStatusCode(), "Unexpected status code");
        assertTrue(cookies.containsKey("auth_sid"), "Response doesn't have 'auth_sid' cookie");
        assertTrue(headers.hasHeaderWithName("x-csrf-token"), "Response doen't havve 'x-csrf-token' header");
        assertTrue(responseGetAuth.jsonPath().getInt("user_id")>0, "User id should be greater then 0");

        JsonPath responseCheckAuth = RestAssured
            .given()
            .relaxedHTTPSValidation()
            .header("x-csrf-token", responseGetAuth.getHeader("x-csrf-token"))
            .cookie("auth_sid", responseGetAuth.getCookie("auth_sid"))
            .get("https://playground.learnqa.ru/api/user/auth")
            .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdOnCheck > 0, "Unexpected user_id " + userIdOnCheck);
        assertEquals(userIdOnAuth, userIdOnCheck, "user_id from auth request is not equal to user_id from check request");
    }
}