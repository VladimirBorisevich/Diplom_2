package ru.yandex.burger.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.burger.model.Order;

import static io.restassured.RestAssured.given;

public class OrderApi extends BaseApi{
    private String ordersPath = "api/orders/";
    @Step("Создание заказа с токеном")
    public Response createOrder(Order order, String token){
        return given()
                .header("authorization",  token)
                .spec(spec)
                .body(order)
                .post(ordersPath);
    }
    @Step("Создание заказа без токена")
    public Response createOrder(Order order){
        return given()
                .spec(spec)
                .body(order)
                .post(ordersPath);
    }
    @Step("Получение заказов пользователя без токена")
    public Response getUserOrders(){
        return given()
                .spec(spec)
                .get(ordersPath);
    }
    @Step("Получение заказов пользователя с токеном")
    public Response getUserOrders(String token){
        return given()
                .header("authorization", token)
                .spec(spec)
                .get(ordersPath);
    }
}
