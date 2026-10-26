package ru.scooter;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersListTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Список заказов возвращается в теле ответа")
    public void ordersListIsReturned() {
        orderClient.getOrders()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}