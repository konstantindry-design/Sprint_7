package ru.scooter;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final List<String> color;
    private OrderClient orderClient;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Object[][] colorVariants() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        };
    }

    @Test
    @DisplayName("Заказ можно создать с разными вариантами цвета")
    public void orderCanBeCreated() {
        orderClient = new OrderClient();
        Order order = new Order("Иван", "Иванов", "Ленинградский проспект, 1", 4,
                "+79990001122", 3, "2026-11-01", "Позвонить за час", color);

        orderClient.create(order)
                .statusCode(201)
                .body("track", notNullValue());
    }
}