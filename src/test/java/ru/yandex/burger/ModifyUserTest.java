package ru.yandex.burger;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.burger.client.UserApi;
import ru.yandex.burger.helper.UserBuilder;
import ru.yandex.burger.model.User;

import static org.hamcrest.Matchers.equalTo;

public class ModifyUserTest {
    User user;
    UserApi userApi = new UserApi();
    Response createdUser;
    Response modifiedUser;
    String accessToken;

    @Before
    public void setUp() {
        user = UserBuilder.getDefaultUser();
        createdUser = userApi.createUser(user);
        accessToken = userApi.loginUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение почты пользователя без авторизации")
    @Description("Данные пользователя не изменены, код ответа 401")
    public void modifyUserWithoutAuth() {
        user.setEmail("burger415329@gmail.com");
        modifiedUser = userApi.modifyUser(user);
        modifiedUser.then()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Изменение почты пользователя с авторизацией")
    @Description("Данные пользователя изменены, код ответа 200")
    public void modifyUserWithAuth() {
        user.setEmail("burger415329@gmail.com");
        modifiedUser = userApi.modifyUser(user, accessToken);
        modifiedUser.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
