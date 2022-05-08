package com.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;


public class UserRequest extends Base {

    @Step("Создание пользователя")
    public Response createUserResponse (User user) {
        return given()
                .spec(getBaseSpec())
                .body(user.inputDataMapForCreateUser())
                .when()
                .post("auth/register");
    }

    @Step("Авторизация пользователя")
    public Response authorizationUserResponse(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user.inputDataMapForAuthorization())
                .when()
                .post("auth/login");
    }

    @Step("Авторизация пользователя")
    public Response authorizationUserResponse(Map<String, String> inputData) {
        return given()
                .spec(getBaseSpec())
                .body(inputData)
                .when()
                .post("auth/login");
    }

    @Step("Выход пользователя из системы")
    public Response logoutResponse(Map<String, String> logoutDataMap) {
        return given()
                .spec(getBaseSpec())
                .body(logoutDataMap)
                .when()
                .post("auth/logout");
    }

    @Step("Получение данных пользователя")
    public Response getUserDataResponse(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .get("auth/user");
    }

    @Step("Изменение данных пользователя")
    public Response changingUserDataResponse(String accessToken, Map<String, String> inputData) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(inputData)
                .when()
                .patch("auth/user");
    }

    @Step("Изменение данных пользователя")
    public Response changingUserDataResponse(Map<String, String> inputData) {
        return given()
                .spec(getBaseSpec())
                .body(inputData)
                .when()
                .patch("auth/user");
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete("auth/user")
                .then()
                .statusCode(SC_ACCEPTED);
    }
}