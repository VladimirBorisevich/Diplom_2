package ru.yandex.burger.helper;

import ru.yandex.burger.model.Order;

import java.util.Random;

public class OrderBuilder {
    public static Order getDefaultOrder(){
        return new Order("61c0c5a71d1f82001bdaaa6d");
    }

    public static Order getEmptyOrder(){
        return new Order();
    }

    public static Order getRandomOrder(){
        Random random = new Random();
        return new Order("" + random.nextInt(10000000));
    }
}
