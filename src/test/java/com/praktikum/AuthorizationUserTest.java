package com.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.*;

public class AuthorizationUserTest {

    public UserRequest userRequest;
    public User user;
    private String accessToken;

    @Before
    public void setup() {
        userRequest = new UserRequest();
        user = User.getRandom();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userRequest.deleteUser(accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Successful user Authorization")
    @Description("Успешная авторизация пользователя")
    public void testSuccessfulUserAuthorization() {
        Response responseCreate = userRequest.createUserResponse(user);
        accessToken = responseCreate.body().as(CreateUserResponse.class).getAccessToken();
        responseCreate.then().statusCode(SC_OK);

        Response responseAuthorization = userRequest.authorizationUserResponse(user);
        responseAuthorization.then().statusCode(SC_OK);
        Assert.assertTrue("Неверное тело ответа", responseAuthorization.body().as(CreateUserResponse.class).isSuccess());
    }

    @Test
    @DisplayName("Authorization with invalid login")
    @Description("Авторизация с не коректным логином")
    public void testAuthorizationInvalidLogin() {
        Response responseAuthorization = userRequest.authorizationUserResponse(user);
        responseAuthorization.then().statusCode(SC_UNAUTHORIZED);
        ResponseErrorMessage bodyResponseErrorMessage = responseAuthorization.body().as(ResponseErrorMessage.class);

        Map<String, String> authorizationInvalidLoginDataMap = new HashMap<>();
        authorizationInvalidLoginDataMap.put("success", "false");
        authorizationInvalidLoginDataMap.put("message", "email or password are incorrect");

        Assert.assertEquals("Не верное тело ответа", authorizationInvalidLoginDataMap.toString(), bodyResponseErrorMessage.toString());
    }

    @Test
    @DisplayName("Authorization with invalid password")
    @Description("Авторизация с не коректным паролем")
    public void testAuthorizationInvalidPassword() {
        Response responseCreate = userRequest.createUserResponse(user);
        accessToken = responseCreate.body().as(CreateUserResponse.class).getAccessToken();
        responseCreate.then().statusCode(SC_OK);

        Map<String, String> inputDataMap = new HashMap<>();
        inputDataMap.put("email", user.email);
        inputDataMap.put("password", "not correct");

        Response responseAuthorization = userRequest.authorizationUserResponse(inputDataMap);
        responseAuthorization.then().statusCode(SC_UNAUTHORIZED);
        ResponseErrorMessage bodyResponseErrorMessage = responseAuthorization.body().as(ResponseErrorMessage.class);

        Map<String, String> authorizationInvalidPasswordDataMap = new HashMap<>();
        authorizationInvalidPasswordDataMap.put("success", "false");
        authorizationInvalidPasswordDataMap.put("message", "email or password are incorrect");

        Assert.assertEquals("Не верное тело ответа", authorizationInvalidPasswordDataMap.toString(), bodyResponseErrorMessage.toString());
    }
}
