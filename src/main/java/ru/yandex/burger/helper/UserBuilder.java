package ru.yandex.burger.helper;

import ru.yandex.burger.model.User;

public class UserBuilder {
    public static User getDefaultUser() {
        return new User("burger41532@gmail.com", "qwerty12345", "burgernyi");
    }

    public static User getUserWithoutEmail() {
        return new User(null, "qwerty12345", "burgernyi");
    }

    public static User getUserWithoutPassword() {
        return new User("burger41532@gmail.com", null, "burgernyi");
    }

    public static User getUserWithoutName() {
        return new User("burger41532@gmail.com", "qwerty12345", null);
    }
}
