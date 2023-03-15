package ru.yandex.burger;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.burger.client.UserApi;
import ru.yandex.burger.helper.UserBuilder;
import ru.yandex.burger.model.User;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    User user;
    UserApi userApi = new UserApi();
    Response createdUser;
    String accessToken;

    @Test
    @DisplayName("Создание пользователя")
    @Description("Пользователь создан, код ответа 200")
    public void createUser() {
        user = UserBuilder.getDefaultUser();
        createdUser = userApi.createUser(user);
        createdUser.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя, который уже существует")
    @Description("Пользователь не создан, код ответа 403")
    public void createAlreadyExistedUser() {
        user = UserBuilder.getDefaultUser();
        userApi.createUser(user);
        createdUser = userApi.createUser(user);
        createdUser.then()
                .statusCode(403)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    @Description("Пользователь не создан, код ответа 403")
    public void createUserWithoutEmail() {
        user = UserBuilder.getUserWithoutEmail();
        createdUser = userApi.createUser(user);
        createdUser.then()
                .statusCode(403)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Пользователь не создан, код ответа 403")
    public void createUserWithoutName() {
        user = UserBuilder.getUserWithoutName();
        createdUser = userApi.createUser(user);
        createdUser.then()
                .statusCode(403)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Заказ ек создан, код ответа 403")
    public void createUserWithoutPassword() {
        user = UserBuilder.getUserWithoutPassword();
        createdUser = userApi.createUser(user);
        createdUser.then()
                .statusCode(403)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        accessToken = userApi.loginUser(user).then().extract().path("accessToken");
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
