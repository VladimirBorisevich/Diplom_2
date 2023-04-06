package ru.yandex.burger;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.burger.client.OrderApi;
import ru.yandex.burger.client.UserApi;
import ru.yandex.burger.helper.OrderBuilder;
import ru.yandex.burger.helper.UserBuilder;
import ru.yandex.burger.model.Order;
import ru.yandex.burger.model.User;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class GetUserOrdersTest {
    Order order;
    User user;
    Response userOrders;
    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();
    String accessToken;

    @Before
    public void setUp() {
        user = UserBuilder.getDefaultUser();
        userApi.createUser(user);
        accessToken = userApi.loginUser(user).then().extract().path("accessToken");
        order = OrderBuilder.getDefaultOrder();
        orderApi.createOrder(order, accessToken);
    }

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    @Description("Заказы получены, код ответа 200")
    public void getUserOrdersWithAuth() {
        userOrders = orderApi.getUserOrders(accessToken);
        userOrders.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    @Description("Заказы не получены, код ответа 401")
    public void getUserOrdersWithoutAuth() {
        userOrders = orderApi.getUserOrders();
        userOrders.then()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
