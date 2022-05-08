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

public class CreateUserTest {
    public UserRequest userRequest;
    private String accessToken;

    @Before
    public void setup() {
        userRequest = new UserRequest();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userRequest.deleteUser(accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Successful user creation")
    @Description("Успешное создание пользователя")
    public void testSuccessfulCreateUser() {
        User user = User.getRandom();
        Response response = userRequest.createUserResponse(user);
        accessToken = response.body().as(CreateUserResponse.class).getAccessToken();
        response.then()
                .statusCode(SC_OK);
        Assert.assertTrue("Неверное тело ответа", response.body().as(CreateUserResponse.class).isSuccess());
    }

    @Test
    @DisplayName("Creating a user who is already registered")
    @Description("Создание пользователя, который уже зарегистрирован")
    public void testCreateUserAlreadyRegistered() {
        User user = User.getRandom();

        Response response = userRequest.createUserResponse(user);

        accessToken = response.body().as(CreateUserResponse.class).getAccessToken();

        Response responseSecondRegistrationUser = userRequest.createUserResponse(user);
        responseSecondRegistrationUser.then()
                .statusCode(SC_FORBIDDEN);

        ResponseErrorMessage bodyResponseErrorMessage = responseSecondRegistrationUser.body().as(ResponseErrorMessage.class);

        Map<String, String> forbiddenRegisteredDataMap = new HashMap<>();
        forbiddenRegisteredDataMap.put("success", "false");
        forbiddenRegisteredDataMap.put("message", "User already exists");

        Assert.assertEquals("Не верное тело ответа", forbiddenRegisteredDataMap.toString(), bodyResponseErrorMessage.toString());
    }
}
