package com.praktikum;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class UserDataParameterizedTest {

    private String email;
    private String name;

    public UserDataParameterizedTest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getChangingUserData() {
        Faker faker = new Faker();
        return new Object[][] {
                {null, faker.name().username()},
                {faker.internet().emailAddress(), faker.name().username()},
                {faker.internet().emailAddress(),  null},
        };
    }

    public UserRequest userRequest;
    private String accessToken;

    @Before
    public void setup() {
        userRequest = new UserRequest();
    }

    @After
    public void tearDown() {
        if(accessToken != null) {
            userRequest.deleteUser(accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Changing user data with authorization")
    @Description("Изменение данных пользователя с авторизацией")
    public void testChangingUserDataAuthorization() {
        User user = User.getRandom();
        Response responseCreate = userRequest.createUserResponse(user);
        accessToken = responseCreate.body().as(CreateUserResponse.class).getAccessToken();
        responseCreate.then().statusCode(SC_OK);

        Response responseAuthorization = userRequest.authorizationUserResponse(user);
        responseAuthorization.then().statusCode(SC_OK);

        Map<String, String> inputDataMap = new HashMap<>();
        if (email != null){
            inputDataMap.put("email", email);
        }

        if (name != null){
            inputDataMap.put("name", name);
        }

        Response changingUserData = userRequest.changingUserDataResponse(accessToken.substring(7), inputDataMap);
        changingUserData.then().statusCode(SC_OK);
        Assert.assertTrue("Неверное тело ответа", changingUserData.body().as(CreateUserResponse.class).isSuccess());

        CreateUserResponse getDataUser = userRequest.getUserDataResponse(accessToken.substring(7)).body().as(CreateUserResponse.class);
        if (email != null){
            Assert.assertEquals("Email пользователя не изменился", email, getDataUser.getUser().getEmail());
        }

        if (name != null){
            Assert.assertEquals("Имя пользователя не изменилось", name, getDataUser.getUser().getName());
        }
    }

    @Test
    @DisplayName("Changing user data without authorization")
    @Description("Изменение данных пользователя без авторизации")
    public void testChangingUserDataNotAuthorization() {
        User user = User.getRandom();
        Response responseCreate = userRequest.createUserResponse(user);
        accessToken = responseCreate.body().as(CreateUserResponse.class).getAccessToken();
        responseCreate.then().statusCode(SC_OK);

        Map<String, String> inputDataMap = new HashMap<>();
        if (email != null){
            inputDataMap.put("email", email);
        }

        if (name != null){
            inputDataMap.put("name", name);
        }

        Response changingUserData = userRequest.changingUserDataResponse(inputDataMap);
        changingUserData.then().statusCode(SC_UNAUTHORIZED);

        Map<String, String> unauthorizedDataMap = new HashMap<>();
        unauthorizedDataMap.put("success", "false");
        unauthorizedDataMap.put("message", "You should be authorised");

        ResponseErrorMessage bodyResponseErrorMessage = changingUserData.body().as(ResponseErrorMessage.class);
        Assert.assertEquals("Не верное тело ответа",unauthorizedDataMap.toString(), bodyResponseErrorMessage.toString());
    }
}
