package com.praktikum;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;

@RunWith(Parameterized.class)
public class CreateUserParametrizedTest {

    private String email;
    private String password;
    private String name;
    private String accessToken;

    public CreateUserParametrizedTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getUserData() {
        Faker faker = new Faker();
        return new Object[][]{
                {null, faker.internet().password(6, 8), faker.name().username()},
                {faker.internet().emailAddress(), null, faker.name().username()},
                {faker.internet().emailAddress(), faker.internet().password(6, 8), null},
        };
    }

    public UserRequest userRequest;

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
    @DisplayName("Creating a user and not filling in one of the required fields")
    @Description("Создание пользователя и не заполнить одно из обязательных полей")
    public void testCreateUserEmptyFields() {
        User user = new User(email, password, name);

        Response response = userRequest.createUserResponse(user);

        response.then()
                .statusCode(SC_FORBIDDEN);

        ResponseErrorMessage bodyResponseErrorMessage = response.body().as(ResponseErrorMessage.class);

        Map<String, String> forbiddenRegisteredDataMap = new HashMap<>();
        forbiddenRegisteredDataMap.put("success", "false");
        forbiddenRegisteredDataMap.put("message", "Email, password and name are required fields");

        Assert.assertEquals("Не верное тело ответа", forbiddenRegisteredDataMap.toString(), bodyResponseErrorMessage.toString());
    }
}