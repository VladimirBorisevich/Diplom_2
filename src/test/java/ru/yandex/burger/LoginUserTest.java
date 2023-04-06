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

public class LoginUserTest {
    User user;
    UserApi userApi = new UserApi();
    Response createdUser;
    Response loginUser;
    String accessToken;

    @Test
    @DisplayName("Логин существующего пользователя")
    @Description("Логин успешен, код ответа 200")
    public void loginWithExistedUser() {
        user = UserBuilder.getDefaultUser();
        createdUser = userApi.createUser(user);
        loginUser = userApi.loginUser(user);
        loginUser.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин несуществующего пользователя")
    @Description("Логин не успешен, код ответа 401")
    public void loginWithNotExistedUser() {
        user = UserBuilder.getDefaultUser();
        loginUser = userApi.loginUser(user);
        loginUser.then()
                .statusCode(401)
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
