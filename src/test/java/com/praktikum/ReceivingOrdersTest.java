package com.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.*;

public class ReceivingOrdersTest {

    public UserRequest userRequest;
    public OrderRequest orderRequest;
    private String accessToken;

    @Before
    public void setup() {
        userRequest = new UserRequest();
        orderRequest = new OrderRequest();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userRequest.deleteUser(accessToken.substring(7));
        }
    }

    @Test
    @DisplayName("Receiving an order from an authorized user")
    @Description("Получение заказа авторизованного пользователя")
    public void testReceivingOrderFromAuthorizedUser() {
        User user = User.getRandom();
        Response responseCreate = userRequest.createUserResponse(user);
        accessToken = responseCreate.body().as(CreateUserResponse.class).getAccessToken();
        responseCreate.then().statusCode(SC_OK);

        Response responseAuthorization = userRequest.authorizationUserResponse(user);
        responseAuthorization.then().statusCode(SC_OK);
        Order order = new Order(List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa76", "61c0c5a71d1f82001bdaaa71"));
        Response responseOrder = orderRequest.createOrderResponse(accessToken.substring(7), order);
        responseOrder.then().statusCode(SC_OK);

        Response receivingOrderResponse = orderRequest.receivingUserOrdersResponse(accessToken.substring(7));
        receivingOrderResponse.then().statusCode(SC_OK);
        ArrayList orderUserList = receivingOrderResponse.then().extract().path("orders");
        Assert.assertTrue("Список заказов пуст", orderUserList.size() > 0);
        Assert.assertTrue("Список заказов пуст", receivingOrderResponse.body().as(UserOrdersResponse.class).getTotal() > 0);
    }

    @Test
    @DisplayName("Receiving an order from an unauthorized  user")
    @Description("Получение заказа не авторизованного пользователя")
    public void testReceivingOrderFromUnauthorizedUser() {
        Response response = orderRequest.receivingUserOrdersResponse();
        response.then().statusCode(SC_UNAUTHORIZED);

        Map<String, String> unauthorizedDataMap = new HashMap<>();
        unauthorizedDataMap.put("success", "false");
        unauthorizedDataMap.put("message", "You should be authorised");

        ResponseErrorMessage bodyResponseErrorMessage = response.body().as(ResponseErrorMessage.class);
        Assert.assertEquals("Не верное тело ответа", unauthorizedDataMap.toString(), bodyResponseErrorMessage.toString());
    }
}
