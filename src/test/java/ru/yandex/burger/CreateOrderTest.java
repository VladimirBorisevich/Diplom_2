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

public class CreateOrderTest {
    Order order;
    User user;
    Response createdOrder;
    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();
    String accessToken;

    @Before
    public void setUp() {
        user = UserBuilder.getDefaultUser();
        userApi.createUser(user);
        accessToken = userApi.loginUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с продуктами и авторизацей пользователя")
    @Description("Заказ создан, код ответа 200")
    public void createOrderWithIngredientsAndAuth() {
        order = OrderBuilder.getDefaultOrder();
        createdOrder = orderApi.createOrder(order, accessToken);
        createdOrder.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без продуктов и с авторизацей пользователя")
    @Description("Заказ не создан, код ответа 400")
    public void createOrderWithZeroIngredientsAndAuth() {
        order = OrderBuilder.getEmptyOrder();
        createdOrder = orderApi.createOrder(order, accessToken);
        createdOrder.then()
                .statusCode(400)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с продуктами и без авторизации пользователя")
    @Description("Заказ не создан, код ответа 400")
    public void createOrderWithIngredientsAndNoAuth() {
        order = OrderBuilder.getDefaultOrder();
        createdOrder = orderApi.createOrder(order);
        createdOrder.then()
                .statusCode(400)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа без продуктов и без авторизации пользователя")
    @Description("Заказ не создан, код ответа 400")
    public void createOrderWithZeroIngredientsAndNoAuth() {
        order = OrderBuilder.getEmptyOrder();
        createdOrder = orderApi.createOrder(order);
        createdOrder.then()
                .statusCode(400)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неправильным продуктом")
    @Description("Заказ не создан, код ответа 500")
    public void createOrderWithWrongIngredientHashCode() {
        order = OrderBuilder.getRandomOrder();
        createdOrder = orderApi.createOrder(order, accessToken);
        createdOrder.then()
                .statusCode(500);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
