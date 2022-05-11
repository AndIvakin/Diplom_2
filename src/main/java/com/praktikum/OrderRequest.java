package com.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderRequest extends Base {

    @Step("Создание заказа")
    public Response createOrderResponse(String accessToken, Order order) {
        if (accessToken == null){
            return given()
                    .spec(getBaseSpec())
                    .body(order.inputDataMapForOrder())
                    .when()
                    .post("orders");
        }
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(order.inputDataMapForOrder())
                .when()
                .post("orders");
    }

    @Step("Получение заказов авторизованого пользователя")
    public Response receivingUserOrdersResponse (String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .get("orders");
    }

    @Step("Получение заказов не авторизованого пользователя")
    public Response receivingUserOrdersResponse () {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("orders");
    }
}
