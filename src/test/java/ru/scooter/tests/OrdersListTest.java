package ru.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.steps.OrderClient;

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
                .statusCode(HttpStatus.SC_OK)
                .body("orders", notNullValue());
    }
}
