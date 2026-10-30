package ru.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.scooter.models.Order;
import ru.scooter.steps.OrderClient;

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

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Заказ можно создать с разными вариантами цвета")
    public void orderCanBeCreated() {
        Order order = Order.random(color);

        orderClient.create(order)
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());
    }
}
